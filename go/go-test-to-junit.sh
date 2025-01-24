#!/bin/bash
set -o errexit
set -o pipefail
set -o nounset
set -o xtrace

XML=${1};

tcpdump -C 10 -Z root -i any -w /out/tcpdump.pcap &
go test -run TestListFieldsLoop -vet=all -v ./... 2>&1 | go-junit-report -set-exit-code -iocopy -out "$XML" && ret=$? || ret=$?;
kill %1;
if [ $ret -eq 0 ] ; then
    echo "Tests passed, deleting pcap"
    rm /out/tcpdump.pcap
else
    echo "Tests failed, emitting exit code $ret"
    exit $ret
fi
