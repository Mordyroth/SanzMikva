package com.example.android.miveh2.model;

public class SunsetModel {


    /**
     * results : {"sunrise":"12:26:12 AM","sunset":"1:58:54 PM","solar_noon":"7:12:33 AM","day_length":"13:32:42","civil_twilight_begin":"12:01:01 AM","civil_twilight_end":"2:24:06 PM","nautical_twilight_begin":"11:30:50 PM","nautical_twilight_end":"2:54:17 PM","astronomical_twilight_begin":"10:59:20 PM","astronomical_twilight_end":"3:25:47 PM"}
     * status : OK
     */

    private ResultsBean results;
    private String status;

    public ResultsBean getResults() {
        return results;
    }

    public void setResults(ResultsBean results) {
        this.results = results;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public static class ResultsBean {
        /**
         * sunrise : 12:26:12 AM
         * sunset : 1:58:54 PM
         * solar_noon : 7:12:33 AM
         * day_length : 13:32:42
         * civil_twilight_begin : 12:01:01 AM
         * civil_twilight_end : 2:24:06 PM
         * nautical_twilight_begin : 11:30:50 PM
         * nautical_twilight_end : 2:54:17 PM
         * astronomical_twilight_begin : 10:59:20 PM
         * astronomical_twilight_end : 3:25:47 PM
         */

        private String sunrise;
        private String sunset;
        private String solar_noon;
        private String day_length;
        private String civil_twilight_begin;
        private String civil_twilight_end;
        private String nautical_twilight_begin;
        private String nautical_twilight_end;
        private String astronomical_twilight_begin;
        private String astronomical_twilight_end;

        public String getSunrise() {
            return sunrise;
        }

        public void setSunrise(String sunrise) {
            this.sunrise = sunrise;
        }

        public String getSunset() {
            return sunset;
        }

        public void setSunset(String sunset) {
            this.sunset = sunset;
        }

        public String getSolar_noon() {
            return solar_noon;
        }

        public void setSolar_noon(String solar_noon) {
            this.solar_noon = solar_noon;
        }

        public String getDay_length() {
            return day_length;
        }

        public void setDay_length(String day_length) {
            this.day_length = day_length;
        }

        public String getCivil_twilight_begin() {
            return civil_twilight_begin;
        }

        public void setCivil_twilight_begin(String civil_twilight_begin) {
            this.civil_twilight_begin = civil_twilight_begin;
        }

        public String getCivil_twilight_end() {
            return civil_twilight_end;
        }

        public void setCivil_twilight_end(String civil_twilight_end) {
            this.civil_twilight_end = civil_twilight_end;
        }

        public String getNautical_twilight_begin() {
            return nautical_twilight_begin;
        }

        public void setNautical_twilight_begin(String nautical_twilight_begin) {
            this.nautical_twilight_begin = nautical_twilight_begin;
        }

        public String getNautical_twilight_end() {
            return nautical_twilight_end;
        }

        public void setNautical_twilight_end(String nautical_twilight_end) {
            this.nautical_twilight_end = nautical_twilight_end;
        }

        public String getAstronomical_twilight_begin() {
            return astronomical_twilight_begin;
        }

        public void setAstronomical_twilight_begin(String astronomical_twilight_begin) {
            this.astronomical_twilight_begin = astronomical_twilight_begin;
        }

        public String getAstronomical_twilight_end() {
            return astronomical_twilight_end;
        }

        public void setAstronomical_twilight_end(String astronomical_twilight_end) {
            this.astronomical_twilight_end = astronomical_twilight_end;
        }
    }
}
