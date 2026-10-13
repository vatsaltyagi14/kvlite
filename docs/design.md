# kvlite: design doc

> One page. Short bullets, not essays. 
> Every decision needs a one-line "why". "Because Redis does it" doesn't count as a why.

## 1. Goal and non-goals
- What is kvlite, in one sentence?
- What will it deliberately **not** do? (Think about: other value types, clustering, auth, the full RESP3 protocol...)

a1- kvlite is a key value dictionary server that will be stored in memory. it is created for fast lookups rather than a full DB search. many clients can use it over the network and share the same data.
a2- for simplicity, we are keeping the value data types to Strings.Clustering,transactions,replication,pub/sub,auth and tls,resp3 (all of these are out of scope but auth can be added after)

## 2. Commands
- Which commands are supported? For each one: its arguments, what it replies with (which RESP type), and what happens in the edge cases (missing key, wrong number of arguments).
- `redis-cli` sends `PING` and `redis-benchmark` uses `PING`, `SET` and `GET`. Which of those do you need?
- Unknown command: what is the reply?

a1- GET,SET,PING,EXPIRE,TTL,DEL with replies as +PONG,+OK,$digits,:n,$-1,:1,:secs with -ERR. if a user get queries a missing key then they are replied by a $-1 because this reply can never be mapped to a real key. del return the number of keys that were deleted with :n
a2- redis benchmark simulates many clients doing many requests so we need PING,SET and GET.
a1- -ERR unkown command with the message of what went wrong. with command casing mismatches handled by converting everything to upper and keys being sensitive as they are the users data and different casing might mean different things to them.

## 3. Data model
- What is the main structure that holds the data? What are its key and value types?
- What extra information does each entry need besides its value? (Expiry? LRU links? Size?) Where does it live: in the value object, or in separate structures?

a1- hashmap with string and an obj. the key cant be an array of bytes because it cant be a hasmap key cause two identical byte arrays would be two different keys.
a2- the obj can store value, expiry(being absolute time so we wont have to update this param with each second),prev/next for our lru cache links, size for memory accounting.

## 4. Expiry (TTL)
- Lazy: on which operations do you check expiry?
- Active: which data structure finds expired keys without scanning everything? How often does the job run, and on which thread?
- What happens to an old expiry entry when a key is overwritten, deleted, or given a new EXPIRE? (Stale entries.)

a1-every operation that touches a key has to check expiry and expiresat is absolute time cause otherwire we would have to be updating the value object again and again
a2-a priorty queue on the expiry can do this.
a3- after popping a key its expiresat is compared with the current expiresat, and if they differ the entry is stale

## 5. Memory cap and eviction
- How do you measure "memory used"? Write down that it's an approximation, and what it ignores.
- When a write goes over the cap, what gets evicted, and in what order? Which structure makes that O(1)?
- What if a single value is bigger than the whole cap?

a1-key bytes+value bytes+fixed pre entry overhead which is a running total on every write and delte so we dont have to scan, ignores garbage and jvm's own memory
a2- the oldest used entry (both get and set) gets evicted and this can done through an lru cache. we can go with allkeys-lru strategy that evicts keys when the memory gets full for continuous use of the service.
a3- we add some validation on the values being entered, reply with -ERR value too large

## 6. Persistence
- When is a snapshot written (on a timer? on shutdown? on a command)? What format is it?
- On startup, how is it loaded? What about keys that expired while the server was down?
- What if the process crashes **halfway through writing** the snapshot? How do you keep the old snapshot safe?

a1-a snapshot should be scheduled and should be written as serialised bytes. 
a2- on startup if a last snapshot is present that is loaded if not i guess we start from scratch and skip entries which have expired.
a3-dumping technique so that a crash never leaves a half written snapshot

## 7. Networking and concurrency
- How are client connections accepted and handled? How many threads, and why that number?
- Several clients touch the store at once: how is it kept consistent? (One lock? ConcurrentHashMap? Something else?) What does your choice cost?
- What if a client sends half a command and then stalls, or sends garbage?

a1-we handle client connections through tcp. wed be using a fixed thread pool with lcosk around the shared store.
a2-options include global lock, concurrenthashmap,sharding. wed be using thread pools
a3- we need validation on this too to send the client a bad request message back.

## 8. Testing and numbers
- What do the unit tests cover? What does the end-to-end test (a real socket) cover?
- Which numbers will the README report? (For example: ops/sec for GET/SET from `redis-benchmark`, p99 latency, behaviour at the memory cap.)

-unit tests covers how each service class is performing and tests in general should have edge cases included.
-numbers from redis benchmark. kvlite vs real redis. and time to make a snapshot

## 9. Open questions
- Anything you're not sure about yet. That's fine: list it here instead of guessing.
a- maybe the use of docker because we cant run redis benchmark on windows
