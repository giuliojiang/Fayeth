# Program

Parses the command line arguments, stores the arguments, links up to the Engine.

# UndefEngine

Engine has method run().

It has a load balancer mechanism to choose between multiple Strategies.

It can run multiple tasks in parallel if allowed by the current configuration.

# Task

A Task will run the SUT once and collect useful information.

The Task's run() method is synchronous, and will create an Outcome.

# Strategy

Strategies can create random strings, CNFs, or anything else interesting. Return type is a TestableInput.

# Outcome

Outcome of a fuzzing.

Contains

* input
* strategy
* bug

Each interesting input is also associated to the Strategy that created it in order to be able to purge an excessive number of outputs.

Each interesting input is also associated with an arbitrary string representing the type of bug that was identified. This will entirely depend on how the output of the SAT is parsed.

# TestableInput

asString() must return the string representation of the DIMACS input to be given to the SUT.

asCNF() can return a CNF object representing its content, if valid, or null if its content is not valid CNF.

# UndefOutputListener

An implementation of SubprocessListener capable of parsing subprocess output with undefined behaviour.

# Collector

The collector receives interesting fuzz inputs (those that trigger errors) as Outcome objects

# TemporaryResources

Provides temporary file names for temporary resources for saving input files for the SUTs, and utilities to clean unused resources once they have been used.