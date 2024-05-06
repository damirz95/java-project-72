FROM gradle:8.7.0-jdk21

WORKDIR /app

COPY /app .

RUN make -C installDist

CMD ./build/install/app/bin/app.bat