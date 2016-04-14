# Scala Backoff
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/pt.tecnico.dsi/backoff_2.11/badge.svg)](https://maven-badges.herokuapp.com/maven-central/pt.tecnico.dsi/backoff_2.11)
[![Build Status](https://travis-ci.org/ist-dsi/backoff.svg?branch=master)](https://travis-ci.org/ist-dsi/backoff)
[![Codacy Badge](https://api.codacy.com/project/badge/grade/c592901ed72a4da484ea64cae0879213)](https://www.codacy.com/app/IST-DSI/backoff)
[![license](http://img.shields.io/:license-MIT-blue.svg)](LICENSE)

Constant, linear, exponential and fibonacci backoff functions for Scala 2.11

## Usage

TODO

Please refer to [documentation][4] for more details.

## Install

To use `backoff` in an [SBT][1] project, add the following dependency to your `build.sbt`:

```scala
libraryDependencies += "pt.tecnico.dsi" %% "backoff" % "1.0.3"
```

Or in [maven][3], you can add `backoff` to your `pom.xml`:

```xml
<dependency>
    <groupId>pt.tecnico.dsi</groupId>
    <artifactId>backoff_2.11</artifactId>
    <version>1.0.3</version>
</dependency>
```

## Build instructions

`backoff` uses [SBT][1] for building and requires Java 8.

```bash
$ git clone https://github.com/ist-dsi/backoff.git
$ cd backoff
$ sbt update
$ sbt compile
```

Then you can run the tests simply by:

```scala
sbt test
```

## License
Backoff is open source and available under the [MIT license](LICENSE).


[1]: http://www.scala-sbt.org
[2]: https://raw.githubusercontent.com/ist-dsi/backoff/master/LICENSE
[3]: https://maven.apache.org
[4]: http://ist-dsi.github.io/backoff/latest/api/#pt.tecnico.dsi.Backoff$
