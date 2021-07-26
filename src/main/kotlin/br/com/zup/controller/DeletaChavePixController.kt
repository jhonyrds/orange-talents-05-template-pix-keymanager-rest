package br.com.zup.controller

import br.com.zup.DeletaChaveRequest
import br.com.zup.PixDeletaServiceGrpc
import io.grpc.Status
import io.grpc.StatusRuntimeException
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Delete
import io.micronaut.http.exceptions.HttpStatusException
import org.slf4j.LoggerFactory
import java.util.*

@Controller("/api/v1/clientes/{clienteId}")
class DeletaChavePixController(private val deletaChavePixClient: PixDeletaServiceGrpc.PixDeletaServiceBlockingStub) {

    val LOGGER = LoggerFactory.getLogger(this::class.java)

    @Delete("/pix/{pixId}")
    fun deleta(clienteId: UUID, pixId: UUID): HttpResponse<Any> {

        try {
            LOGGER.info("[$clienteId] deletando chave pix com $pixId")
            deletaChavePixClient.deleta(
                DeletaChaveRequest.newBuilder()
                    .setClienteId(clienteId.toString())
                    .setPixId(pixId.toString())
                    .build()
            )
            return HttpResponse.ok(location(clienteId, pixId.toString()))
        } catch (e: StatusRuntimeException) {
            val statusCode = e.status.code
            val description = e.status.description

            if (statusCode == Status.Code.NOT_FOUND) {
                throw HttpStatusException(HttpStatus.NOT_FOUND, description)
            }
            throw HttpStatusException(HttpStatus.BAD_REQUEST, e.message)
        }

    }

    private fun location(clienteId: UUID, pixId: String) = HttpResponse.uri("/api/v1/clientes/$clienteId/pix/${pixId}")
}