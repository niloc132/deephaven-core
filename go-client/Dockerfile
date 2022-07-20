FROM golang:1.18.1

ADD . /project

WORKDIR /project

RUN go mod download

# Note that we don't run tests this way, but leave it as an entrypoint
# to more easily collect results
#RUN go test ./...
