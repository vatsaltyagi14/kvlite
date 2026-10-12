# kvlite: context for Claude

kvlite ("key-value lite") is a small Redis-like key-value store in plain Java 21. It has no frameworks on purpose: the point is to build by hand what frameworks hide (sockets, threads, the protocol, memory management).

## Plan and progress live elsewhere
This is one project inside a larger training plan. The plan, the working rules and the session log are in the `roadmapshi` folder:

@~/OneDrive/Desktop/claude/roadmapshi/CLAUDE.md

- Read `~/OneDrive/Desktop/claude/roadmapshi/PROGRESS.md` at the start of a session, and add the session entry **there**, not here. There is one log for everything.
- Design decisions live in `docs/design.md` in this repo.

## Scope
- GET / SET / DEL / EXPIRE over TCP, speaking a subset of RESP so that `redis-cli` and `redis-benchmark` work against it.
- TTL expiry (lazy + active), LRU eviction under a memory cap, snapshot persistence, multiple clients on a thread pool.
- Stretch: an append-only log.

## Build and test
- `mvn test` runs the JUnit 5 tests. `mvn package` builds the jar.
- Compiled as Java 21 (`maven.compiler.release`), run on JDK 25.

## Rules for this repo
- Vatsal writes the core logic (store, expiry, eviction, protocol parsing, concurrency). Claude scaffolds plumbing (build config, boilerplate tests) and reviews like a strict senior engineer.
- Vatsal types every git command. Changes go through pull requests that Claude reviews once branching has been introduced.
- The README records measured numbers only.
