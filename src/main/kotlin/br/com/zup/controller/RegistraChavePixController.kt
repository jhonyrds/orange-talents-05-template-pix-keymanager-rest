package br.com.zup.controller

import br.com.zup.PixKeymanagerGrpcServiceGrpc
import br.com.zup.request.ChavePixRequest
import io.grpc.Status
import io.grpc.StatusRuntimeException
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Post
import io.micronaut.http.exceptions.HttpStatusException
import io.micronaut.validation.Validated
import org.slf4j.LoggerFactory
import java.util.*
import javax.validation.Valid

@Validated
@Controller("/api/v1/clientes/{clienteId}")
class RegistraChavePixController(private val registraChavePixClient: PixKeymanagerGrpcServiceGrpc.PixKeymanagerGrpcServiceBlockingStub) {

    val LOGGER = LoggerFactory.getLogger(this::class.java)

    @Post("pix")
    fun registra(clienteId: UUID, @Valid @Body request: ChavePixRequest): HttpResponse<Any> {
        LOGGER.info("[$clienteId] registrando uma nova chave pix com $request")
        try {

            val response = registraChavePixClient.registra(request.toModel(clienteId))
            return HttpResponse.created(location(clienteId, response.pixId))

        } catch (e: StatusRuntimeException) {
            val statusCode = e.status.code
            val description = e.status.description

            if (statusCode == Status.Code.INVALID_ARGUMENT) {
                throw HttpStatusException(HttpStatus.BAD_REQUEST, description)
            }
            if (statusCode == Status.Code.ALREADY_EXISTS) {
                throw HttpStatusException(HttpStatus.UNPROCESSABLE_ENTITY, description)
            }
            throw HttpStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.message)
        }
    }

    private fun location(clienteId: UUID, pixId: String) = HttpResponse.uri("/api/v1/clientes/$clienteId/pix/${pixId}")
}