javapickling
============

**Java Pickling** is a Java framework for pickling types into target pickled formats and vice versa (i.e. unpickling).
It was inspired by the [Pickling Combinators paper](http://research.microsoft.com/en-us/um/people/akenn/fun/picklercombinators.pdf), and by the [Scala Pickling project](https://github.com/scala/pickling).

The design allows pickling into multiple pickled formats - JSON and byte[] implementations are provided.

At present it does not provide automatic of pickling of custom classes, ergo pickling of those types must be implemented by the user. The custom picklers only need be defined once - the same pickler will be used regardless of the pickled format.

See ByteIOPicklerTest.java and JsonPicklerTest.java for example usage, however once set up this illustrates the API usage:

    void test(House house) {}
        // Create a pickler.
        Pickler<House, JsonNode> pickler = jsonPickler.object(House.class);

        // Pickle a sample House object into a JsonNode.
        JsonNode node = pickler.pickle(house, null);

        // Unpickle the JsonNode back into a House.
        House house2 = pickler.unpickle(node);
    }

## Quick Start

TBD...
