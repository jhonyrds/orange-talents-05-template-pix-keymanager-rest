package br.com.zup.controller

import br.com.zup.ConsultaChaveRequest
import br.com.zup.PixConsultaChaveServiceGrpc
import br.com.zup.response.DetalhaChaveResponse
import io.grpc.Status
import io.grpc.StatusRuntimeException
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.exceptions.HttpStatusException
import org.slf4j.LoggerFactory
import java.util.*

@Controller("/api/v1/clientes/{clienteId}")
class DetalhaChavePixController(private val detalhaChavePixClient: PixConsultaChaveServiceGrpc.PixConsultaChaveServiceBlockingStub) {

    private val LOGGER = LoggerFactory.getLogger(
        this::class.java
    )

    @Get("/pix/{pixId}")
    fun detalha(clienteId: UUID, pixId: UUID): HttpResponse<Any> {

        try {
            LOGGER.info("[$clienteId] carrega chave pix por id: $pixId")
            val chaveResponse = detalhaChavePixClient.consulta(
                ConsultaChaveRequest.newBuilder()
                    .setPixId(
                        ConsultaChaveRequest.FiltroPorPixId.newBuilder()
                            .setClienteId(clienteId.toString())
                            .setPixId(pixId.toString())
                            .build()
                    )
                    .build()
            )
            return HttpResponse.ok(DetalhaChaveResponse(chaveResponse))

        } catch (e: StatusRuntimeException) {
            val statusCode = e.status.code
            val description = e.status.description

            if (statusCode == Status.Code.NOT_FOUND) {
                throw HttpStatusException(HttpStatus.NOT_FOUND, description)
            }
            throw HttpStatusException(HttpStatus.BAD_REQUEST, e.message)
        }
    }
}