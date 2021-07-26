package br.com.zup.controller

import br.com.zup.DeletaChaveResponse
import br.com.zup.PixDeletaServiceGrpc
import br.com.zup.shared.factory.KeyGrpcClientFactory
import io.micronaut.context.annotation.Factory
import io.micronaut.context.annotation.Replaces
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.mockito.BDDMockito.given
import org.mockito.Mockito.any
import org.mockito.Mockito.mock
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@MicronautTest(transactional = false)
internal class DeletaChavePixControllerTest {

    @field:Inject
    lateinit var deletaStub: PixDeletaServiceGrpc.PixDeletaServiceBlockingStub

    @field:Inject
    @field:Client("/")
    lateinit var client: HttpClient

    @Test
    internal fun `deve deletar uma chave pix`() {
        //cenário
        val clienteId = UUID.randomUUID().toString()
        val pixId = UUID.randomUUID().toString()

        //ação
        val responseGrpc = DeletaChaveResponse.newBuilder()
            .setPixId(pixId)
            .setClienteId(clienteId)
            .build()

        given(deletaStub.deleta(any())).willReturn(responseGrpc)

        val request = HttpRequest.DELETE<Any>("/api/v1/clientes/$clienteId/pix/$pixId")
        val response = client.toBlocking().exchange(request, Any::class.java)

        //verificação
        assertEquals(HttpStatus.OK, response.status)
    }

    @Factory
    @Replaces(factory = KeyGrpcClientFactory::class)
    internal class MockitoStubFactory {

        @Singleton
        fun deletaChave() = mock(PixDeletaServiceGrpc.PixDeletaServiceBlockingStub::class.java)
    }
}