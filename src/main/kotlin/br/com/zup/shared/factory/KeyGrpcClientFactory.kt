package br.com.zup.shared.factory

import br.com.zup.PixKeymanagerGrpcServiceGrpc
import io.grpc.ManagedChannel
import io.micronaut.context.annotation.Factory
import io.micronaut.grpc.annotation.GrpcChannel
import javax.inject.Singleton

@Factory
class KeyGrpcClientFactory(@GrpcChannel("keyManager") val channel: ManagedChannel) {

    @Singleton
    fun registraChave() = PixKeymanagerGrpcServiceGrpc.newBlockingStub(channel)

}