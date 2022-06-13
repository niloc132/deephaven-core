FROM golang:1.18.1

ADD . /project

WORKDIR /project

RUN go mod download

RUN go test ./...

