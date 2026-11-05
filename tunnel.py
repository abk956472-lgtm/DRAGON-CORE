
import time
from datetime import datetime


class DragonTunnel:

    def __init__(self, duration=300):
        self.duration = duration
        self.active = False
        self.start_time = None

    def start(self):
        print("=" * 40)
        print("       DRAGON TUNNEL ENGINE")
        print("=" * 40)

        self.active = True
        self.start_time = time.time()

        print("[DRAGON] Tunnel session initialized")
        print("[DRAGON] Duration: 300 seconds")
        print("[DRAGON] Waiting for authorized connection...")

        while self.active:
            elapsed = time.time() - self.start_time

            if elapsed >= self.duration:
                break

            remaining = self.duration - int(elapsed)
            now = datetime.now().strftime("%H:%M:%S")

            print(
                f"[{now}] SESSION ACTIVE | "
                f"Remaining: {remaining}s"
            )

            time.sleep(5)

        self.stop()

    def stop(self):
        self.active = False
        print("[DRAGON] Tunnel session stopped")


if __name__ == "__main__":
    tunnel = DragonTunnel()
    tunnel.start()
