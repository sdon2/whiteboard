package packet;
/**
 * Enum to represent different types of Packets
 *
 */
public enum PacketType {
    // announce that you are a new client in the game   
    NEW_CLIENT, // client to server
    
    // announce the creation of a new board
    NEW_BOARD, // client to server
    
    // client is ready to accept drawing commmands
    CLIENT_READY, // client to server
    
    // announce that a client has just joined a board
    JOIN_BOARD, // client to server and server to client

    // announce that a client has just exited a board
    EXIT_BOARD, // client to server and server to client
    
    // the entire board sent from the server to the client
    // also includes a list of the clients
    BOARD_MODEL, // server to client
    
    // all of the users in the board
    BOARD_USERS, // server to client
    
    // all the boards in the game
    BOARD_IDENTIFIER_LIST, // server to client
    
    // all the boards on the server
    // a message to draw something on a board
    DRAW_COMMAND, // sever to client and client to server
}