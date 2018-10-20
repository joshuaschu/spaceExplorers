package de.joshuaschulz.connection;

public interface AsyncAPICall {
    void onSuccess(String result);
    default void onFailure(Exception exception) {}
    default void onBefore() {};
    default void onAfter() {};
}
