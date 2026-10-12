# kvlite: design doc

> One page. Short bullets, not essays. For each section, answer the questions in your own words, then delete them.
> Every decision needs a one-line "why". "Because Redis does it" doesn't count as a why.

## 1. Goal and non-goals
- What is kvlite, in one sentence?
- What will it deliberately **not** do? (Think about: other value types, clustering, auth, the full RESP3 protocol...)

## 2. Commands
- Which commands are supported? For each one: its arguments, what it replies with (which RESP type), and what happens in the edge cases (missing key, wrong number of arguments).
- `redis-cli` sends `PING` and `redis-benchmark` uses `PING`, `SET` and `GET`. Which of those do you need?
- Unknown command: what is the reply?

## 3. Data model
- What is the main structure that holds the data? What are its key and value types?
- What extra information does each entry need besides its value? (Expiry? LRU links? Size?) Where does it live: in the value object, or in separate structures?

## 4. Expiry (TTL)
- Lazy: on which operations do you check expiry?
- Active: which data structure finds expired keys without scanning everything? How often does the job run, and on which thread?
- What happens to an old expiry entry when a key is overwritten, deleted, or given a new EXPIRE? (Stale entries.)

## 5. Memory cap and eviction
- How do you measure "memory used"? Write down that it's an approximation, and what it ignores.
- When a write goes over the cap, what gets evicted, and in what order? Which structure makes that O(1)?
- What if a single value is bigger than the whole cap?

## 6. Persistence
- When is a snapshot written (on a timer? on shutdown? on a command)? What format is it?
- On startup, how is it loaded? What about keys that expired while the server was down?
- What if the process crashes **halfway through writing** the snapshot? How do you keep the old snapshot safe?

## 7. Networking and concurrency
- How are client connections accepted and handled? How many threads, and why that number?
- Several clients touch the store at once: how is it kept consistent? (One lock? ConcurrentHashMap? Something else?) What does your choice cost?
- What if a client sends half a command and then stalls, or sends garbage?

## 8. Testing and numbers
- What do the unit tests cover? What does the end-to-end test (a real socket) cover?
- Which numbers will the README report? (For example: ops/sec for GET/SET from `redis-benchmark`, p99 latency, behaviour at the memory cap.)

## 9. Open questions
- Anything you're not sure about yet. That's fine: list it here instead of guessing.
