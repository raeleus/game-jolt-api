package com.github.raeleus.gamejoltapi;

public interface GameJoltListener {
    /**
     * Called when a response is received from the Game Jolt server.
     * @param data The data processed from the jsonValue containing all the return values. The data should be
     * casted to the data class implementing {@link GameJoltData} that matches the submitted {@link
     * GameJoltRequest}.
     */
    void response(GameJoltData data);

    /**
     * The HTTP request may fail if there was a timeout or some other related issue.
     * @param t
     */
    void failed(Throwable t);

    /**
     * The connection was cancelled or the response has an unexpected number of results.
     */
    void cancelled();
}
