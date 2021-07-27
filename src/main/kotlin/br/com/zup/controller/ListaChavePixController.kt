package br.com.zup.controller

import br.com.zup.ListaChavesRequest
import br.com.zup.PixListaChavesServiceGrpc
import br.com.zup.response.ChavePixResponse
import io.grpc.StatusRuntimeException
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.exceptions.HttpStatusException
import org.slf4j.LoggerFactory
import java.util.*

@Controller("/api/v1/clientes/")
class ListaChavePixController(private val listaChaveClient: PixListaChavesServiceGrpc.PixListaChavesServiceBlockingStub) {

    val LOGGER = LoggerFactory.getLogger(this::class.java)

    @Get("{clienteId}/pix/")
    fun lista(clienteId: UUID): HttpResponse<Any> {

        try {
            LOGGER.info("[$clienteId] - Listando todas as chaves")
            val pix = listaChaveClient.lista(
                ListaChavesRequest.newBuilder().setClienteId(clienteId.toString()).build()
            )

            val chaves = pix.chavesList.map { ChavePixResponse(it) }
            return HttpResponse.ok(chaves)

        } catch (e: StatusRuntimeException) {
            throw HttpStatusException(HttpStatus.NOT_FOUND, "Ocorreu um erro ao litar as chaves")
        }
    }
}