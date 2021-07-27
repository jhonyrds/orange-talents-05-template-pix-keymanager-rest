package br.com.zup.response

import br.com.zup.ListaChavesResponse
import io.micronaut.core.annotation.Introspected
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset

@Introspected
class ChavePixResponse(chave: ListaChavesResponse.ChavePix) {

    val id = chave.pixId
    val chave = chave.chave
    val tipo = chave.tipo
    val tipoDeConta = chave.tipoDeConta
    val criadaEm = chave.criadaEm.let {
        LocalDateTime.ofInstant(Instant.ofEpochSecond(it.seconds, it.nanos.toLong()), ZoneOffset.UTC)
    }
}
