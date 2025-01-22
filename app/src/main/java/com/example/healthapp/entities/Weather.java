package com.example.healthapp.entities;

import org.json.JSONException;
import org.json.JSONObject;

public class Weather {
    private String img;
    private String temperature;
    private String info;
    private int condition;

    public static Weather fromJson(JSONObject jsonObject) {

        try {
            Weather weather = new Weather();
            weather.setCondition(jsonObject.getJSONArray("weather").getJSONObject(0).getInt("id"));
            weather.setImg(updateWeatherImg(weather.getCondition()));
            weather.setInfo(jsonObject.getJSONArray("weather").getJSONObject(0).getString("main"));
            double tempResult = jsonObject.getJSONObject("main").getDouble("temp") - 273.15;
            int roundedValue = (int) Math.rint(tempResult);
            weather.setTemperature(Integer.toString(roundedValue));
            return weather;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static String updateWeatherImg(int condition) {
        if (condition >= 0 && condition <= 300) {
            return "thunderstrom1";
        } else if (condition >= 300 && condition <= 500) {
            return "lightrain";
        } else if (condition >= 500 && condition <= 600) {
            return "shower";
        } else if (condition >= 600 && condition <= 700) {
            return "snow2";
        } else if (condition >= 701 && condition <= 771) {
            return "fog";
        } else if (condition >= 772 && condition <= 800) {
            return "overcast";
        } else if (condition == 800) {
            return "sunny";
        } else if (condition >= 801 && condition <= 804) {
            return "cloudy";
        } else if (condition >= 900 && condition <= 902) {
            return "thunderstrom1";
        }
        if (condition == 903) {
            return "snow1";
        }
        if (condition == 904) {
            return "sunny";
        }
        if (condition >= 905 && condition <= 1000) {
            return "thunderstrom2";
        }

        return "dunno";
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public int getCondition() {
        return condition;
    }

    public void setCondition(int condition) {
        this.condition = condition;
    }

    public String getTemperature() {
        return temperature + " ℃";
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }
}
