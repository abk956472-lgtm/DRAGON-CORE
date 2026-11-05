
import time
import socket
from datetime import datetime

class DragonCore:

    def __init__(self):
        self.name = "DRAGON-CORE"
        self.duration = 300
        self.interval = 5

    def check_internet(self):
        try:
            socket.create_connection(
                ("1.1.1.1", 443),
                timeout=5
            )
            return True
        except OSError:
            return False

    def start(self):
        print("=" * 35)
        print("       DRAGON-CORE v1.0")
        print("       EMERGENCY SYSTEM")
        print("=" * 35)

        start_time = time.time()

        while time.time() - start_time < self.duration:

            status = self.check_internet()
            now = datetime.now().strftime("%H:%M:%S")

            if status:
                print(f"[{now}] INTERNET: ONLINE")
            else:
                print(f"[{now}] INTERNET: OFFLINE")

            time.sleep(self.interval)

        print("DRAGON-CORE: TEST COMPLETED")


if __name__ == "__main__":
    dragon = DragonCore()
    dragon.start()
