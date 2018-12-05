import subprocess
from subprocess import run
import matplotlib.pyplot as plt
import numpy as np
# -----------------------------------------------------------------------------|
def main():
    """

    """
    res = [540 * i for i in range(1, 11)]
    time_seq = []
    time_par = []

    for file in ["RayTraceSmpDo", "RayTraceSeq"]:
        print(file + ":")
        for i, resolution in enumerate([str(540 * i) for i in range(1, 11)]):
            print("{0}) Resolution = {1} x {1}".format(i+1, resolution))
            result = run(["java", "-cp", ".:../../pj2.jar", "pj2",
                         "debug=makespan", file, resolution, resolution],
                            stderr = subprocess.PIPE)
            print("Time = {}ms".format(result.stderr.decode('utf-8').split()[3]))
            print()

        if file == "RayTraceSeq":
            time_seq.append(float(result.stderr.decode('utf-8').split()[3]))
        else:
            time_par.append(float(result.stderr.decode('utf-8').split()[3]))

        print()


    plt.title("Strong scaling")
    plt.ylabel("Time in seconds")
    plt.xlabel("Resolution")
    plt.plot(res, np.array(time_seq)/1000, label = "Sequential")
    plt.plot(res, np.array(time_seq)/1000, label = "parallel")
    plt.legend()
    plt.savefig("strong_scaling.png", bbox_inches = "tight")
# -----------------------------------------------------------------------------|


if __name__ == '__main__':
    main()
