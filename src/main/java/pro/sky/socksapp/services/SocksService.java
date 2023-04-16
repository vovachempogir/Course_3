package pro.sky.socksapp.services;

import pro.sky.socksapp.model.Color;
import pro.sky.socksapp.model.Size;
import pro.sky.socksapp.model.Sock;

public interface SocksService {

    Sock addSocks(Sock sock, long quantity);

    Sock editSock(Sock sock, long quantity);

    Integer getSocks(Color color, Size size, int cottonMin, int cottonMax);

    boolean deleteSocks(Sock sock, long quantity);

}
