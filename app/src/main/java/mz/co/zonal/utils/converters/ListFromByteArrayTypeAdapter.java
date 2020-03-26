package mz.co.zonal.utils.converters;

import android.util.JsonToken;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

public class ListFromByteArrayTypeAdapter extends TypeAdapter<List<byte[]>> {


    @Override
    public void write(JsonWriter out, List<byte[]> value) throws IOException {

    }

    @Override
    public List<byte[]> read(JsonReader in) throws IOException {
        return null;
    }
}