
from main import DragonCore
from network import DragonNetwork
from tunnel import DragonTunnel


class DragonSystem:

    def __init__(self):
        self.name = "DRAGON CORE"
        self.version = "1.0.0"

        self.network = DragonNetwork()
        self.tunnel = DragonTunnel(duration=300)

    def start(self):
        print("=" * 45)
        print("          🐉 DRAGON CORE")
        print("          EMERGENCY SYSTEM")
        print("          VERSION 1.0.0")
        print("=" * 45)

        print("\n[1] Checking network...")
        online = self.network.scan()

        if online:
            print("\n[DRAGON] Network connection detected.")
        else:
            print("\n[DRAGON] No internet route detected.")

        print("\n[2] Emergency session manager ready.")
        print("[3] Tunnel engine initialized.")

        print("\n[DRAGON] System initialization completed.")


if __name__ == "__main__":
    system = DragonSystem()
    system.start()
