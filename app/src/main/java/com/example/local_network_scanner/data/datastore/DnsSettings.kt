package com.example.local_network_scanner.data.datastore

data class DnsSettings(
    val dnsMode: String = "DEFAULT",
    val customDnsIp: String = "",
    val enableSecureDns: Boolean = false
)
