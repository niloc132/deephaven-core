#!/bin/bash
set -o errexit
set -o pipefail
set -o nounset
set -o xtrace

XML=${1};

tcpdump -i any -w /out/tcpdump.pcap &
go test -vet=all -v ./... 2>&1 | go-junit-report -set-exit-code -iocopy -out "$XML";
kill %1