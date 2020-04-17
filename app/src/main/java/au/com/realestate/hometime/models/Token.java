package au.com.realestate.hometime.models;

import com.squareup.moshi.Json;

public class Token {
    @Json(name = "DeviceToken")
    public String value;
}
