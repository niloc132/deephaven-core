#!/bin/bash
set -eux;

XML=${1};

go test -vet=all -v ./... 2>&1 | go-junit-report -set-exit-code -iocopy -out "$XML";
