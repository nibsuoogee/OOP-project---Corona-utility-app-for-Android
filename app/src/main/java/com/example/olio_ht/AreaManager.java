package com.example.olio_ht;

import static android.content.ContentValues.TAG;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;

import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class AreaManager {
    float reference_timestamp = 1.5775704e12f;
    private ArrayList<InfectionWeek> InfectionList = null;
    private ArrayList<VaccinationWeek> VaccinationList = null;
    private ArrayList<AreaCode> areaCodeListInf = null;
    private ArrayList<AreaCode> areaCodeListVac = null;
    private List<BarEntry> infectionsList = new ArrayList<>();
    private List<BarEntry> vaccinationsList = new ArrayList<>();
    private ArrayList<String> labelList = new ArrayList<>();
    private ArrayList<String> weekList = new ArrayList<>();
    private String latestWeek;
    private Timestamp timestamp;

    private static AreaManager am = null;

    public static AreaManager getInstance() {
        if (am == null) {
            am = new AreaManager();
        }
        return am;
    }

    private AreaManager() {
        areaCodeListInf = new ArrayList<>();
        areaCodeListVac = new ArrayList<>();
        URL urlInfectionIds = null;
        URL urlVaccinationIds = null;
        try {
            urlInfectionIds = new URL("https://sampo.thl.fi/pivot/prod/fi/epirapo/covid19case/" +
                    "fact_epirapo_covid19case.dimensions.json");
            areaCodeListInf = readJSONid(urlInfectionIds);
            urlVaccinationIds = new URL("https://sampo.thl.fi/pivot/prod/fi/vaccreg/cov19cov/" +
                    "fact_cov19cov.dimensions.json");
            areaCodeListVac = readJSONid(urlVaccinationIds);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    public String getInfection(String label) {
        InfectionWeek ew = InfectionList.stream()
                .filter(ew1 -> ew1.getLabelValue().equals(label))
                .findFirst()
                .orElse(null);
        return(ew.getValueValue());
    }

    public String getVaccination(String label) {
        VaccinationWeek vw = VaccinationList.stream()
                .filter(vw1 -> vw1.getLabelValue().equals(label))
                .findFirst()
                .orElse(null);
        try {
            vw.getValueValue();
        } catch (NullPointerException e){
            String zero = "0";
            return(zero);
        }
        return(vw.getValueValue());
    }

    public List<BarEntry> getInfections() {
        infectionsList.clear();
        for(InfectionWeek ew: InfectionList) {
            if (ew.getValueValue() != null) {
                String Infections = ew.getValueValue();
                String InfRegex = ew.getLabelValue().replaceAll("[a-zA-Z ]","");
                if (Infections.equals("..") || Infections.equals("0")) {
                    continue;
                }
                if (!InfRegex.equals("")) {
                    SimpleDateFormat df = new SimpleDateFormat("yyyyww");
                    try {
                        Date parsedDate = df.parse(InfRegex);
                        timestamp = new Timestamp(parsedDate.getTime());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    infectionsList.add(new BarEntry(
                            (new Long(timestamp.getTime()).floatValue()-reference_timestamp),
                            Integer.parseInt(Infections)));
                }
            }
        }
        return(infectionsList);
    }

    public List<BarEntry> getVaccinations() {
        vaccinationsList.clear();
        for(VaccinationWeek vw: VaccinationList) {
            if (vw.getValueValue() != null) {
                String Vaccinations = vw.getValueValue();
                String VacRegex = vw.getLabelValue().replaceAll("[a-zA-Z ]","");
                if (Vaccinations.equals("..") || Vaccinations.equals("0")) {
                    continue;
                }
                if (!VacRegex.equals("")) {
                    SimpleDateFormat df = new SimpleDateFormat("yyyyww");
                    try {
                        Date parsedDate = df.parse(VacRegex);
                        timestamp = new Timestamp(parsedDate.getTime());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    vaccinationsList.add(new BarEntry(
                            (new Long(timestamp.getTime()).floatValue()-reference_timestamp+1e8f),
                            Integer.parseInt(Vaccinations)));
                }
            }
        }
        return(vaccinationsList);
    }

    public ArrayList<String> getLabels() {
        for(AreaCode ac: areaCodeListInf) {
            labelList.add(ac.getLabel());
        }
        return(labelList);
    }

    public ArrayList<String> getWeeks() {
        for(InfectionWeek ew: InfectionList) {
            if (ew.getValueValue() != null) {
                weekList.add(ew.getLabelValue());
                if (!ew.getLabelValue().equals("Kaikki ajat")) {
                    latestWeek = ew.getLabelValue();
                }
            }
        }
        return(weekList);
    }

    public String getLatestWeek() {
        return(latestWeek);
    }

    public boolean readInfectionJSON (String label) throws JSONException {
        URL url = null;
        AreaCode acl = areaCodeListInf.stream()
                .filter(acl1 -> acl1.getLabel().equals(label))
                .findFirst()
                .orElse(null);
        try {
            url = new URL("https://sampo.thl.fi/pivot/prod/fi/epirapo/covid19case/fact_epirapo_covid19case.json?row="
                    +acl.getId()+"-"+acl.getSid()+".&column=dateweek20200101-509030&filter=measure-444833");
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return(false);
        } catch (NullPointerException e) {
            e.printStackTrace();
            return(false);
        }
        String json= getJSON(url);
        JSONObject jObject = null;
        try {

            jObject = new JSONObject(json);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JSONObject jIndex = null;
        try {
            jIndex = jObject.getJSONObject("dataset")
                    .getJSONObject("dimension")
                    .getJSONObject("dateweek20200101")
                    .getJSONObject("category")
                    .getJSONObject("index");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JSONObject jLabel = null;
        try {
            jLabel = jObject.getJSONObject("dataset")
                    .getJSONObject("dimension")
                    .getJSONObject("dateweek20200101")
                    .getJSONObject("category")
                    .getJSONObject("label");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        InfectionList = new ArrayList<>();
        for(int i = 0; i<jIndex.names().length(); i++){
            InfectionWeek iw = new InfectionWeek(jIndex.names().getString(i),
                    jIndex.get(jIndex.names().getString(i)).toString(),
                    jLabel.get(jLabel.names().getString(i)).toString());
            InfectionList.add(iw);
        }

        JSONObject jValue = null;
        try {
            jValue = jObject.getJSONObject("dataset")
                    .getJSONObject("value");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        for(int i = 0; i<jValue.names().length(); i++){
            String newValuekey = jValue.names().getString(i);
            String newValueValue = jValue.get(jValue.names().getString(i)).toString();
            InfectionWeek iw = InfectionList.stream()
                    .filter(ew1 -> ew1.getIndexValue().equals(newValuekey))
                    .findFirst()
                    .orElse(null);
            if (iw != null) {
                iw.setValueValue(newValueValue);
            }
        }
        return(true);
    }

    public boolean readVaccinationJSON (String label) throws JSONException {
        URL url = null;
        AreaCode acl = areaCodeListVac.stream()
                .filter(acl1 -> acl1.getLabel().equals(label))
                .findFirst()
                .orElse(null);
        try {
            url = new URL("https://sampo.thl.fi/pivot/prod/fi/vaccreg/cov19cov/fact_cov19cov.json?row="
                    +acl.getId()+"-"+acl.getSid()+".&column=dateweek20201226-525425&filter=measure-533175");
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return(false);
        } catch (NullPointerException e) {
            e.printStackTrace();
            return(false);
        }
        String json= getJSON(url);
        JSONObject jObject = null;
        try {
            jObject = new JSONObject(json);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JSONObject jIndex = null;
        try {
            jIndex = jObject.getJSONObject("dataset")
                    .getJSONObject("dimension")
                    .getJSONObject("dateweek20201226")
                    .getJSONObject("category")
                    .getJSONObject("index");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JSONObject jLabel = null;
        try {
            jLabel = jObject.getJSONObject("dataset")
                    .getJSONObject("dimension")
                    .getJSONObject("dateweek20201226")
                    .getJSONObject("category")
                    .getJSONObject("label");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        VaccinationList = new ArrayList<>();
        for(int i = 0; i<jIndex.names().length(); i++){
            VaccinationWeek vw = new VaccinationWeek(jIndex.names().getString(i),
                    jIndex.get(jIndex.names().getString(i)).toString(),
                    jLabel.get(jLabel.names().getString(i)).toString());
            VaccinationList.add(vw);
        }

        JSONObject jValue = null;
        try {
            jValue = jObject.getJSONObject("dataset")
                    .getJSONObject("value");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        for(int i = 0; i<jValue.names().length(); i++){
            String newValuekey = jValue.names().getString(i);
            String newValueValue = jValue.get(jValue.names().getString(i)).toString();
            VaccinationWeek vw = VaccinationList.stream()
                    .filter(ew1 -> ew1.getIndexValue().equals(newValuekey))
                    .findFirst()
                    .orElse(null);
            if (vw != null) {
                vw.setValueValue(newValueValue);
            }
        }
        return(true);
    }

    public ArrayList<AreaCode> readJSONid (URL url) throws JSONException {
        String json = getJSON(url);
        JSONArray jArray = null;
        json = json.replaceFirst("thl.pivot.loadDimensions"+"[(]","");
        json = json.replace("$[)][;]","");
        System.out.println("json: "+json);
        try {
            jArray = new JSONArray(json);
        } catch (JSONException e) {
            System.out.println("%%%%%%%%%%%%%%%%%%%%%%%% FAIL");
            e.printStackTrace();
        }

        JSONArray jSHP = null;
        try {
            jSHP = jArray.getJSONObject(0)
                    .getJSONArray("children")
                    .getJSONObject(0)
                    .getJSONArray("children");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        ArrayList<AreaCode> areaCodeList = new ArrayList<>();
        for(int i = 0; i<jSHP.length(); i++){
            for(int j = 0; j<jSHP.getJSONObject(i).getJSONArray("children").length(); j++) {
                JSONObject jArea = jSHP.getJSONObject(i).getJSONArray("children").getJSONObject(j);
                String newIdValue = jArea.getString("id");
                String newSidValue = jArea.getString("sid");
                String newLabelValue = jArea.getString("label");
                AreaCode aw = new AreaCode(newIdValue,newSidValue,newLabelValue);
                areaCodeList.add(aw);
            }
        }
        return(areaCodeList);
    }

    public String getJSON(URL url) {
        String response = null;
        try {
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            InputStream in = new BufferedInputStream(conn.getInputStream());
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            StringBuilder sb = new StringBuilder();
            String line = null;
            while((line = br.readLine()) != null) {
                sb.append(line).append("");
            }
            response = sb.toString();
            in.close();

        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return response;
    }

}
