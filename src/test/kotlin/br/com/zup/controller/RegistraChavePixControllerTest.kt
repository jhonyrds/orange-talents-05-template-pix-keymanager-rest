package br.com.zup.controller

import br.com.zup.PixKeymanagerGrpcServiceGrpc
import br.com.zup.RegistraChavePixResponse
import br.com.zup.request.ChavePixRequest
import br.com.zup.request.TipoDeChaveRequest.EMAIL
import br.com.zup.request.TipoDeContaRequest.CONTA_CORRENTE
import br.com.zup.shared.factory.KeyGrpcClientFactory
import io.micronaut.context.annotation.Factory
import io.micronaut.context.annotation.Replaces
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.mockito.BDDMockito.given
import org.mockito.Mockito
import org.mockito.Mockito.mock
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@MicronautTest
internal class RegistraChavePixControllerTest {

    @field:Inject
    lateinit var registraStub: PixKeymanagerGrpcServiceGrpc.PixKeymanagerGrpcServiceBlockingStub

    @field:Inject
    @field:Client("/")
    lateinit var client: HttpClient

    @Test
    internal fun `deve registrar uma nova chave pix`() {

        //cenário
        val clienteId = UUID.randomUUID().toString()
        val pixId = UUID.randomUUID().toString()

        //ação
        val responseGrpc = RegistraChavePixResponse.newBuilder()
            .setClienteId(clienteId)
            .setPixId(pixId)
            .build()

        given(registraStub.registra(Mockito.any())).willReturn(responseGrpc)

        val chavePix = ChavePixRequest(
            tipoDeConta = CONTA_CORRENTE,
            chave = "teste@email.com",
            tipoDeChave = EMAIL
        )

        val request = HttpRequest.POST("/api/v1/clientes/$clienteId/pix", chavePix)
        val response = client.toBlocking().exchange(request, ChavePixRequest::class.java)

        assertEquals(HttpStatus.CREATED, response.status)
        assertTrue(response.headers.contains("Location"))
        assertTrue(response.header("Location")!!.contains(pixId))

    }

    @Factory
    @Replaces(factory = KeyGrpcClientFactory::class)
    internal class MockitoStubFactory {

        @Singleton
        fun stubMock() = mock(PixKeymanagerGrpcServiceGrpc.PixKeymanagerGrpcServiceBlockingStub::class.java)
    }
}