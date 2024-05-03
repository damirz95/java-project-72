clean:
	make -C app clean
build:
	make -C app build
install:
	make -C app install
start:
	make -C app start

test:
	make -C app test

lint:
	make -C app lint

report:
	make -C app report