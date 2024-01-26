package com.github.raeleus.gamejoltapi;

public interface GameJoltListener {
    /**
     * Called when a response is received from the Game Jolt server.
     * @param value The value processed from the jsonValue containing all the return values. The value should be
     * casted to the value class implementing {@link GameJoltValue} that matches the submitted {@link
     * GameJoltRequest}.
     */
    void response(GameJoltValue value);

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
