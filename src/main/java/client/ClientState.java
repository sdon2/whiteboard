package client;
/**
 * Enum to represent various client states, options are IDLE and PLAYING
 *
 */
public enum ClientState { 
    // Client is loading a board or waiting to load a board.
    IDLE, 
    
    // Client is playing on a board.
    PLAYING
}
