package pro.sky.socksapp.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pro.sky.socksapp.model.Color;
import pro.sky.socksapp.model.Size;
import pro.sky.socksapp.model.Sock;
import pro.sky.socksapp.services.SocksService;

@RestController
@RequestMapping("/socks")
@Tag(name = "Носки", description = "CRUD-операции и другие эндпоинты для работы с носками")
public class SocksController {
    private final SocksService socksService;

    public SocksController(SocksService socksService) {
        this.socksService = socksService;
    }

    @PostMapping()
    @Operation(
            summary = "Поступление новых носков",
            description = "Регистрация новой партии носков"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Носки были успешно зарегистрированы",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema =
                                    @Schema(implementation = Sock.class))
                            )
                    }
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Bad request. Missing or incorrect format request parameters",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema =
                                    @Schema(implementation = Sock.class))
                            )
                    }
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema =
                                    @Schema(implementation = Sock.class))
                            )
                    }
            )

    })
    public ResponseEntity<Sock> createSock(@RequestBody Sock sock, @RequestParam int quantity) {
        Sock createSock = socksService.addSocks(sock, quantity);
        return ResponseEntity.ok(createSock);
    }

    @PutMapping
    @Operation(
            summary = "Продажи носков",
            description = "Продажа носков со склада"
    )
    @Parameters(value = {
            @Parameter(name = "quantity", example = "1")
    })
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Носки успешно проданны",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = Sock.class))
                            )
                    }
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "В наличии нет носков.Неккоректный запрос. Отсутствующие или неправильные параметры запроса ",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema =
                                    @Schema(implementation = Sock.class))
                            )
                    }
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema =
                                    @Schema(implementation = Sock.class))
                            )
                    }
            )
    })
    public ResponseEntity<Sock> editSocks(@RequestBody Sock sock, @RequestParam int quantity) {
        Sock editSocks = socksService.editSock(sock, quantity);
        if (editSocks == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(editSocks);
    }

    @GetMapping()
    @Operation(
            summary = "Найдите количество носков по количеству  хлопка в составе",
            description = "Показать количество носков по параметру"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Носки успешно найденны",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema =
                                    @Schema(implementation = Sock.class))
                            )
                    }
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Неккоректный запрос. Отсутствующие или неправильные параметры запроса ",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema =
                                    @Schema(implementation = Sock.class))
                            )
                    }
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema =
                                    @Schema(implementation = Sock.class))
                            )
                    }
            )
    })
    public ResponseEntity<Integer> getSock(@RequestParam Color color,
                                           @RequestParam Size size,
                                           @RequestParam int cottonMin,
                                           @RequestParam int cottonMax) {
        int count = socksService.getSocks(color, size, cottonMin, cottonMax);
        return ResponseEntity.ok(count);
    }

    @DeleteMapping()
    @Operation(
            summary = "Списание испорченной продукции",
            description = "Создание списания испорченной продукции"
    )
    @Parameters(value = {
            @Parameter(name = "quantity", example = "1")
    })
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Списание испорченной продукции успешно",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = Sock.class))
                            )
                    }
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "В наличии нет носков.Неккоректный запрос. Отсутствующие или неправильные параметры запроса ",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema =
                                    @Schema(implementation = Sock.class))
                            )
                    }
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema =
                                    @Schema(implementation = Sock.class))
                            )
                    }
            )
    })
    public ResponseEntity<Void> deleteSocks(@RequestBody Sock sock, @RequestParam int quantity) {
        if (socksService.deleteSocks(sock, quantity)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}
