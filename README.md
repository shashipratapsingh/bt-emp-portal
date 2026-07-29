



                             # Apache Kafka Integration - Employee Management System

## What is Kafka?

Apache Kafka is an open-source distributed event streaming platform used to exchange data asynchronously between applications.

Instead of one application directly calling another, applications communicate by publishing and consuming events through Kafka.

---

# Why Kafka?

1. High Throughput
   - Kafka can process millions of messages per second.

2. Fault Tolerance
   - Kafka replicates data across brokers to prevent data loss.

3. Durable Storage
   - Messages are stored on disk until the configured retention period expires.

4. Scalability
   - Kafka can be scaled horizontally by adding more brokers.

5. Asynchronous Communication
   - Producer and Consumer work independently.

6. Event-Driven Architecture
   - Services communicate using events instead of direct API calls.

7. Reliability
   - Kafka guarantees reliable message delivery.

---

# Kafka Architecture

```
Producer
    │
    ▼
+-------------------------+
|      Kafka Broker       |
|                         |
|  Topic                  |
|   ├── Partition 0        |
|   ├── Partition 1        |
|   └── Partition 2        |
+-------------------------+
          │
          ▼
      Consumer
```

---

# Kafka Components

## 1. Producer

The Producer publishes messages/events to Kafka Topics.

Example:

```
Employee Service
       │
       ▼
Employee Created Event
```

---

## 2. Consumer

The Consumer subscribes to Kafka Topics and consumes messages.

Example:

```
Employee Consumer
        │
        ▼
Receives Employee Event
        │
        ▼
Send Welcome Email
```

---

## 3. Broker

A Broker is a Kafka Server.

A Kafka Cluster may contain one or more Brokers.

Example:

```
Broker 1

Broker 2

Broker 3
```

---

## 4. Topic

A Topic is used to categorize messages.

Example:

```
employee-created-topic
```

Think of a Topic like a database table where events are stored.

---

## 5. Partition

A Topic is divided into one or more Partitions.

```
Employee Topic

Partition 0

Partition 1

Partition 2
```

Partitions allow Kafka to process messages in parallel.

---

## 6. Offset

Every message inside a partition has a unique Offset.

Example:

```
Offset

0

1

2

3

4

5
```

Offsets help Kafka identify which messages have already been consumed.

---

## 7. Consumer Group

Consumers belong to a Consumer Group.

Kafka distributes partitions among consumers in the same group.

Example:

```
employee-email-group
```

---

# Kafka Flow Used in Our Project

```
Employee API
      │
      ▼
Employee Service
      │
      ▼
Kafka Producer
      │
      ▼
employee-created-topic
      │
      ▼
Kafka Consumer
      │
      ▼
Java Mail Sender
      │
      ▼
Welcome Email
```

---

# Kafka Installation (KRaft Mode)

> Note:
> This project uses **Kafka KRaft Mode (Without ZooKeeper).**

---

## Step 1 - Download Kafka

Download Kafka from the official Apache Kafka website.

Version Used:

```
kafka_2.13-4.3.1
```

---

## Step 2 - Extract Kafka

Extract Kafka into any folder.

Example:

```
C:\kafka_2.13-4.3.1
```

---

## Step 3 - Generate Cluster ID

```
bin\windows\kafka-storage.bat random-uuid
```

Example Output

```
cJ4Z7EooRU2bpjH_r3vv9Q
```

---

## Step 4 - Format Kafka Storage

```
bin\windows\kafka-storage.bat format --config config\broker.properties --cluster-id <cluster-id>
```

---

## Step 5 - Start Controller

```
bin\windows\kafka-server-start.bat config\controller.properties
```

---

## Step 6 - Start Broker

```
bin\windows\kafka-server-start.bat config\broker.properties
```

---

## Step 7 - Create Kafka Topic

```
bin\windows\kafka-topics.bat --create --topic employee-created-topic --bootstrap-server localhost:9092
```

---

## Step 8 - List Topics

```
bin\windows\kafka-topics.bat --list --bootstrap-server localhost:9092
```

---

# Spring Boot Integration

## Dependencies

- spring-kafka
- spring-boot-starter-mail

---

## Kafka Producer

Class Created:

```
EmployeeProducer
```

Responsibilities

- Publish Employee Event
- Send Event to Kafka Topic

---

## Kafka Consumer

Class Created:

```
EmployeeConsumer
```

Responsibilities

- Listen to Kafka Topic
- Receive Employee Event
- Send Welcome Email

---

## Kafka Topic Configuration

Class Created:

```
KafkaTopicConfig
```

Responsibilities

- Create Kafka Topic
- Configure Topic Name

---

## Event Model

Class Created:

```
EmployeeEvent
```

Fields

```
employeeId

fullName

email
```

---

# Email Notification Flow

```
Create Employee
       │
       ▼
Save Employee
       │
       ▼
Publish Event
       │
       ▼
Kafka Topic
       │
       ▼
Consumer
       │
       ▼
Send Welcome Email
```

---

# Retry Mechanism (Implemented)

Class Created

```
KafkaConsumerConfig
```

Configuration Used

```
DefaultErrorHandler

FixedBackOff

Retry Count : 3

Retry Interval : 5 Seconds
```

Retry Flow

```
Receive Event
      │
      ▼
Send Email
      │
      ▼
FAILED
      │
      ▼
Wait 5 Seconds
      │
      ▼
Retry 1
      │
      ▼
FAILED
      │
      ▼
Retry 2
      │
      ▼
FAILED
      │
      ▼
Retry 3
      │
      ▼
FAILED
      │
      ▼
Retries Exhausted
```

Important

The Consumer throws a RuntimeException whenever email sending fails.

Kafka receives the exception and automatically retries according to the configured `DefaultErrorHandler`.

---

# Features Implemented

- ✅ Kafka Producer
- ✅ Kafka Consumer
- ✅ Kafka Topic Configuration
- ✅ Employee Event Model
- ✅ Email Notification
- ✅ Asynchronous Communication
- ✅ Retry Mechanism using `DefaultErrorHandler`
- ✅ Fixed BackOff Strategy (5 Seconds × 3 Retries)

---

# Upcoming Features

- ⬜ Dead Letter Topic (DLT)
- ⬜ Retry Topics
- ⬜ Exponential BackOff
- ⬜ Kafka UI Monitoring
- ⬜ Consumer Lag Monitoring
- ⬜ Prometheus Integration
- ⬜ Grafana Dashboard
- ⬜ Multiple Brokers
- ⬜ Multiple Partitions
- ⬜ JSON Serialization
- ⬜ Exception Classification
- ⬜ Metrics & Monitoring

---

# Project Flow Summary

```
Employee API
      │
      ▼
Employee Service
      │
      ▼
Kafka Producer
      │
      ▼
employee-created-topic
      │
      ▼
Kafka Broker
      │
      ▼
Kafka Consumer
      │
      ▼
Retry Mechanism
      │
      ▼
Java Mail Sender
      │
      ▼
Welcome Email Sent
```

