package br.com.zup.controller

import br.com.zup.ListaChavesResponse
import br.com.zup.PixListaChavesServiceGrpc
import br.com.zup.TipoDeChave.*
import br.com.zup.TipoDeConta.CONTA_CORRENTE
import br.com.zup.shared.factory.KeyGrpcClientFactory
import com.google.protobuf.Timestamp
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

import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@MicronautTest
internal class ListaChavePixControllerTest {

    @field:Inject
    lateinit var listaChaveStub: PixListaChavesServiceGrpc.PixListaChavesServiceBlockingStub

    @field:Inject
    @field:Client("/")
    lateinit var client: HttpClient


    @Test
    internal fun `deve listar todas chaves cadastradas`() {

        //cenário
        val clienteId = UUID.randomUUID().toString()

        //ação
        val responseGrpc = listaChavesResponse(clienteId)

        given(listaChaveStub.lista(Mockito.any())).willReturn(responseGrpc)

        val request = HttpRequest.GET<Any>("/api/v1/clientes/$clienteId/pix/")
        val response = client.toBlocking().exchange(request, List::class.java)

        //validações
        assertEquals(HttpStatus.OK, response.status)
        assertNotNull(response.body())
        assertEquals(response.body().size, 4)


    }

    private fun listaChavesResponse(clienteId: String): ListaChavesResponse {
        val chaveTipoCelular = ListaChavesResponse.ChavePix.newBuilder()
            .setPixId(UUID.randomUUID().toString())
            .setTipo(CELULAR)
            .setChave("+5511999999999")
            .setTipoDeConta(CONTA_CORRENTE)
            .setCriadaEm(LocalDateTime.now().let {
                val createdAt = it.atZone(ZoneId.of("UTC")).toInstant()
                Timestamp.newBuilder()
                    .setSeconds(createdAt.epochSecond)
                    .setNanos(createdAt.nano)
                    .build()
            })
            .build()

        val chaveTipoAleatoria = ListaChavesResponse.ChavePix.newBuilder()
            .setPixId(UUID.randomUUID().toString())
            .setTipo(ALEATORIA)
            .setChave("")
            .setTipoDeConta(CONTA_CORRENTE)
            .setCriadaEm(LocalDateTime.now().let {
                val createdAt = it.atZone(ZoneId.of("UTC")).toInstant()
                Timestamp.newBuilder()
                    .setSeconds(createdAt.epochSecond)
                    .setNanos(createdAt.nano)
                    .build()
            })
            .build()

        val chaveTipoCpf = ListaChavesResponse.ChavePix.newBuilder()
            .setPixId(UUID.randomUUID().toString())
            .setTipo(CPF)
            .setChave("30221240071")
            .setTipoDeConta(CONTA_CORRENTE)
            .setCriadaEm(LocalDateTime.now().let {
                val createdAt = it.atZone(ZoneId.of("UTC")).toInstant()
                Timestamp.newBuilder()
                    .setSeconds(createdAt.epochSecond)
                    .setNanos(createdAt.nano)
                    .build()
            })
            .build()

        val chaveTipoEmail = ListaChavesResponse.ChavePix.newBuilder()
            .setPixId(UUID.randomUUID().toString())
            .setTipo(EMAIL)
            .setChave("teste@teste.com.br")
            .setTipoDeConta(CONTA_CORRENTE)
            .setCriadaEm(LocalDateTime.now().let {
                val createdAt = it.atZone(ZoneId.of("UTC")).toInstant()
                Timestamp.newBuilder()
                    .setSeconds(createdAt.epochSecond)
                    .setNanos(createdAt.nano)
                    .build()
            })
            .build()

        return ListaChavesResponse.newBuilder()
            .setClienteId(clienteId)
            .addAllChaves(listOf(chaveTipoCelular, chaveTipoAleatoria, chaveTipoCpf, chaveTipoEmail))
            .build()
    }

    @Factory
    @Replaces(factory = KeyGrpcClientFactory::class)
    internal class MockitoStubFactory {

        @Singleton
        fun listaMock() = mock(PixListaChavesServiceGrpc.PixListaChavesServiceBlockingStub::class.java)
    }
}