package me.gsqfi.fitask.fitask;

import com.google.gson.JsonDeserializer;
import com.google.gson.JsonSerializer;

public interface IAdapter<T> extends JsonSerializer<T>, JsonDeserializer<T> {
}
