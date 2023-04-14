package pro.sky.socksapp.services.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;
import pro.sky.socksapp.model.Color;
import pro.sky.socksapp.model.Size;
import pro.sky.socksapp.model.Sock;
import pro.sky.socksapp.services.SocksFilesService;
import pro.sky.socksapp.services.SocksService;

import javax.annotation.PostConstruct;
import java.io.File;
import java.util.HashMap;
import java.util.Map;


@Service
public class SocksServiceImpl implements SocksService {

    private final SocksFilesService socksFilesService;
    private static HashMap<Sock , Long> socks = new HashMap<>();

    public SocksServiceImpl(SocksFilesService socksFilesService) {
        this.socksFilesService = socksFilesService;
    }

    @PostConstruct
    private void init(){
        File file = socksFilesService.getDataFile();
        if (file.exists()){
            socksFilesService.readFromFile();
        }
    }

    @Override
    public Sock addSocks(Sock sock, long quantity) {
        if (quantity > 0 && sock.getCottonPart() > 0 && sock.getCottonPart() <= 100) {
            socks.merge(sock, quantity, Long::sum);
            socks.put(sock, quantity);
            saveToFile();
        }
        return sock;
    }

    @Override
    public Sock editSock(Sock sock, long quantity) {
        ObjectUtils.isNotEmpty(socks);
        if (quantity > 0 && socks.containsKey(sock)) {
            long number = socks.get(sock) - quantity;
            if (number >= 0) {
                socks.merge(sock, quantity, (a, b) -> a - b);
                socks.put(sock, quantity);
                saveToFile();
            } else {
                throw new UnsupportedOperationException("Носков нет в наличии");
            }
        }
        return sock;
    }

    @Override
    public Integer getSocks(Color color, Size size, int cottonMin, int cottonMax) {
        ObjectUtils.isNotEmpty(socks);
        int count = 0;
        if (cottonMin >= 0 && cottonMax >= 0 && cottonMax >= cottonMin) {
            for (Map.Entry<Sock, Long> entry : socks.entrySet()) {
                if (entry.getKey().getColor() == color && entry.getKey().getSize() == size &&
                        entry.getKey().getCottonPart() >= cottonMin && entry.getKey().getCottonPart() <= cottonMax) {
                    count += entry.getValue();
                }
            }
        }
        return count;
    }

    @Override
    public boolean deleteSocks(Sock sock, long quantity) {
        ObjectUtils.isNotEmpty(socks);
        if (socks.containsKey(sock)) {
            socks.remove(sock, quantity);
            saveToFile();
            return true;
        }
        return false;
    }


    private void saveToFile() {
        try {
            String json = new ObjectMapper().writeValueAsString(socks);
            socksFilesService.saveToFile(json);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private void readFromFile() {
        try {
            String json = socksFilesService.readFromFile();
            socks = new ObjectMapper().readValue(json, new TypeReference<HashMap<Sock, Long>>() {
            });
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
