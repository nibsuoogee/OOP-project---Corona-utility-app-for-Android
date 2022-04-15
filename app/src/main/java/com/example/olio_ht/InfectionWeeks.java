/*package com.example.olio_ht;

import com.google.gson.annotations.SerializedName;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InfectionWeeks {
    @SerializedName("dataset")
    Dataset mdataset;
    public InfectionWeeks(Dataset dataset) {
        mdataset = dataset;
    }


    public class Dataset {
        private String version;
        @SerializedName("class")
        private String clas;
        private String label;
        Dimension dimension;
        Value value;
    }

    public class Dimension {
        List<String> id;
        List<String> size;
        @SerializedName("hcdmunicipality2020")
        Hcd hcd;
        @SerializedName("dateweek20200101")
        Dawe dawe;
    }

    public class Hcd {
        Category category;
    }

    public class Dawe {
        Category category;
    }

    public class Category {
        Index index;
        Label label;
    }

    public class Index {
        //String index;
        //List<String> indexes;
    }

    public class Label {
        //String label;
    }


    public class Value {
        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        @SerializedName("0")
        String value;
    }
}

public class InfectionWeeks {
    Dataset dataset;

    public class Dataset {
        private String version;
        @SerializedName("class")
        private String clas;
        private String label;
        Dimension dimension;
        private Value value;
    }

    public class Dimension {
        List<String> id;
        List<String> size;
        @SerializedName("hcdmunicipality2020")
        Hcd hcd;
        @SerializedName("dateweek20200101")
        Dawe dawe;
    }

    public class Hcd {
        Category category;
    }

    public class Dawe {
        Category category;
    }

    public class Category {
        Index index;
        Label label;
    }

    public class Index {
        //String index;
        //List<String> indexes;
    }

    public class Label {
        //String label;
    }

    public class Value {
        @SerializedName("0")
        String value;
    }

}*/