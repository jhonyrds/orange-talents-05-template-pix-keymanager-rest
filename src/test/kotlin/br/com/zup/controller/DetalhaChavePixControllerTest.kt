package br.com.zup.controller

import br.com.zup.ConsultaChaveResponse
import br.com.zup.PixConsultaChaveServiceGrpc
import br.com.zup.shared.factory.KeyGrpcClientFactory
import io.micronaut.context.annotation.Factory
import io.micronaut.context.annotation.Replaces
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.mockito.BDDMockito.given
import org.mockito.Mockito
import org.mockito.Mockito.mock
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@MicronautTest
internal class DetalhaChavePixControllerTest {

    @field:Inject
    lateinit var detalhaChaveStub: PixConsultaChaveServiceGrpc.PixConsultaChaveServiceBlockingStub

    @field:Inject
    @field:Client("/")
    lateinit var client: HttpClient

    val CLIENTE_ID = UUID.randomUUID().toString()
    val PIX_ID = UUID.randomUUID().toString()

    @Test
    internal fun `deve detalhar uma chave pix existente`() {

        //cenário

        //ação
        given(detalhaChaveStub.consulta(Mockito.any())).willReturn(
            ConsultaChaveResponse.newBuilder().setClienteId(CLIENTE_ID).setPixId(PIX_ID).build()
        )

        val request = HttpRequest.GET<Any>("/api/v1/clientes/$CLIENTE_ID/pix/$PIX_ID")
        val response = client.toBlocking().exchange(request, Any::class.java)

        //verificação
        assertEquals(HttpStatus.OK, response.status)
        assertNotNull(response.body())
    }

    @Factory
    @Replaces(factory = KeyGrpcClientFactory::class)
    internal class MockitoStubFactory {

        @Singleton
        fun detalhaMock() = mock(PixConsultaChaveServiceGrpc.PixConsultaChaveServiceBlockingStub::class.java)
    }
}