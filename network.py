
import socket
import time
from datetime import datetime


class DragonNetwork:

    SERVERS = [
        ("1.1.1.1", 443),
        ("8.8.8.8", 443),
        ("9.9.9.9", 443)
    ]

    def test_server(self, host, port):
        start = time.perf_counter()

        try:
            connection = socket.create_connection(
                (host, port), timeout=3
            )
            connection.close()

            latency = (time.perf_counter() - start) * 1000

            return {
                "online": True,
                "latency": round(latency, 2)
            }

        except OSError:
            return {
                "online": False,
                "latency": None
            }

    def scan(self):
        print("\n🐉 DRAGON NETWORK ENGINE")
        print("=" * 35)

        available = 0

        for host, port in self.SERVERS:
            result = self.test_server(host, port)
            timestamp = datetime.now().strftime("%H:%M:%S")

            if result["online"]:
                available += 1
                print(
                    f"[{timestamp}] {host} "
                    f"ONLINE | {result['latency']} ms"
                )
            else:
                print(f"[{timestamp}] {host} OFFLINE")

        print("-" * 35)
        print(f"Available servers: {available}/3")

        return available > 0


if __name__ == "__main__":
    network = DragonNetwork()
    network.scan()
