package me.gsqfi.fitaskextra.fitaskextra.Main.palyerdata;

import me.gsqfi.fitask.fitask.api.taskcomponent.conditions.ICondition;

public interface IData {
    <T extends ICondition<?>> T getData(String playerName, Class<T> clas);
    <T extends ICondition<?>> void set(String playerName,T value);
}
