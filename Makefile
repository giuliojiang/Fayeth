all:
	./build.sh
	./res/get_banosat

clean:
	find . -name "*.class" -type f -delete

purge: clean
	rm -rf res/banosat