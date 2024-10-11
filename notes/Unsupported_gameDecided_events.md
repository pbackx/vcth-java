Currently, the parser will skip `gameDeciced` events with any state not equal to `WINNER_DECIDED`. During processing of
the files, I have found a very limited number of `DRAW` and `UNSUPPORTED` states. For now, I am simply ignoring them.

Here are a few examples of those events that can work as a starting point should I want to support them in the future:

```json
{
  "state": "DRAW",
  "spikeMode": {
    "currentRound": 2,
    "attackingTeam": {
      "value": 18
    },
    "completedRounds": [
      {
        "roundNumber": 1,
        "spikeModeResult": {
          "attackingTeam": {
            "value": 18
          },
          "cause": "ELIMINATION",
          "defendingTeam": {
            "value": 19
          }
        },
        "winningTeam": {
          "value": 19
        }
      }
    ],
    "roundsToWin": 13,
    "defendingTeam": {
      "value": 19
    }
  }
}
```

```json
{
  "state": "UNSUPPORTED",
  "spikeMode": {
    "currentRound": 2,
    "attackingTeam": {
      "value": 18
    },
    "completedRounds": [
      {
        "roundNumber": 1,
        "spikeModeResult": {
          "attackingTeam": {
            "value": 18
          },
          "cause": "ELIMINATION",
          "defendingTeam": {
            "value": 19
          }
        },
        "winningTeam": {
          "value": 19
        }
      }
    ],
    "roundsToWin": 13,
    "defendingTeam": {
      "value": 19
    }
  }
}
```

```json
{
  "state": "DRAW",
  "spikeMode": {
    "currentRound": 10,
    "attackingTeam": {
      "value": 19
    },
    "completedRounds": [
      {
        "roundNumber": 1,
        "spikeModeResult": {
          "attackingTeam": {
            "value": 19
          },
          "cause": "ELIMINATION",
          "defendingTeam": {
            "value": 20
          }
        },
        "winningTeam": {
          "value": 19
        }
      },
      {
        "roundNumber": 2,
        "spikeModeResult": {
          "attackingTeam": {
            "value": 19
          },
          "cause": "DETONATE",
          "defendingTeam": {
            "value": 20
          }
        },
        "winningTeam": {
          "value": 19
        }
      },
      {
        "roundNumber": 3,
        "spikeModeResult": {
          "attackingTeam": {
            "value": 19
          },
          "cause": "ELIMINATION",
          "defendingTeam": {
            "value": 20
          }
        },
        "winningTeam": {
          "value": 19
        }
      },
      {
        "roundNumber": 4,
        "spikeModeResult": {
          "attackingTeam": {
            "value": 19
          },
          "cause": "ELIMINATION",
          "defendingTeam": {
            "value": 20
          }
        },
        "winningTeam": {
          "value": 19
        }
      },
      {
        "roundNumber": 5,
        "spikeModeResult": {
          "attackingTeam": {
            "value": 19
          },
          "cause": "ELIMINATION",
          "defendingTeam": {
            "value": 20
          }
        },
        "winningTeam": {
          "value": 19
        }
      },
      {
        "roundNumber": 6,
        "spikeModeResult": {
          "attackingTeam": {
            "value": 19
          },
          "cause": "ELIMINATION",
          "defendingTeam": {
            "value": 20
          }
        },
        "winningTeam": {
          "value": 19
        }
      },
      {
        "roundNumber": 7,
        "spikeModeResult": {
          "attackingTeam": {
            "value": 19
          },
          "cause": "DETONATE",
          "defendingTeam": {
            "value": 20
          }
        },
        "winningTeam": {
          "value": 19
        }
      },
      {
        "roundNumber": 8,
        "spikeModeResult": {
          "attackingTeam": {
            "value": 19
          },
          "cause": "ELIMINATION",
          "defendingTeam": {
            "value": 20
          }
        },
        "winningTeam": {
          "value": 19
        }
      },
      {
        "roundNumber": 9,
        "spikeModeResult": {
          "attackingTeam": {
            "value": 19
          },
          "cause": "DETONATE",
          "defendingTeam": {
            "value": 20
          }
        },
        "winningTeam": {
          "value": 19
        }
      }
    ],
    "roundsToWin": 13,
    "defendingTeam": {
      "value": 20
    }
  }
}
```

```json
{
  "state": "DRAW",
  "spikeMode": {
    "currentRound": 15,
    "attackingTeam": {
      "value": 19
    },
    "completedRounds": [
      {
        "roundNumber": 1,
        "spikeModeResult": {
          "attackingTeam": {
            "value": 18
          },
          "cause": "ELIMINATION",
          "defendingTeam": {
            "value": 19
          }
        },
        "winningTeam": {
          "value": 18
        }
      },
      {
        "roundNumber": 2,
        "spikeModeResult": {
          "attackingTeam": {
            "value": 18
          },
          "cause": "DETONATE",
          "defendingTeam": {
            "value": 19
          }
        },
        "winningTeam": {
          "value": 18
        }
      },
      {
        "roundNumber": 3,
        "spikeModeResult": {
          "attackingTeam": {
            "value": 18
          },
          "cause": "ELIMINATION",
          "defendingTeam": {
            "value": 19
          }
        },
        "winningTeam": {
          "value": 19
        }
      },
      {
        "roundNumber": 4,
        "spikeModeResult": {
          "attackingTeam": {
            "value": 18
          },
          "cause": "ELIMINATION",
          "defendingTeam": {
            "value": 19
          }
        },
        "winningTeam": {
          "value": 18
        }
      },
      {
        "roundNumber": 5,
        "spikeModeResult": {
          "attackingTeam": {
            "value": 18
          },
          "cause": "ELIMINATION",
          "defendingTeam": {
            "value": 19
          }
        },
        "winningTeam": {
          "value": 19
        }
      },
      {
        "roundNumber": 6,
        "spikeModeResult": {
          "attackingTeam": {
            "value": 18
          },
          "cause": "DETONATE",
          "defendingTeam": {
            "value": 19
          }
        },
        "winningTeam": {
          "value": 18
        }
      },
      {
        "roundNumber": 7,
        "spikeModeResult": {
          "attackingTeam": {
            "value": 18
          },
          "cause": "DETONATE",
          "defendingTeam": {
            "value": 19
          }
        },
        "winningTeam": {
          "value": 18
        }
      },
      {
        "roundNumber": 8,
        "spikeModeResult": {
          "attackingTeam": {
            "value": 18
          },
          "cause": "ELIMINATION",
          "defendingTeam": {
            "value": 19
          }
        },
        "winningTeam": {
          "value": 19
        }
      },
      {
        "roundNumber": 9,
        "spikeModeResult": {
          "attackingTeam": {
            "value": 18
          },
          "cause": "ELIMINATION",
          "defendingTeam": {
            "value": 19
          }
        },
        "winningTeam": {
          "value": 19
        }
      },
      {
        "roundNumber": 10,
        "spikeModeResult": {
          "attackingTeam": {
            "value": 18
          },
          "cause": "ELIMINATION",
          "defendingTeam": {
            "value": 19
          }
        },
        "winningTeam": {
          "value": 19
        }
      },
      {
        "roundNumber": 11,
        "spikeModeResult": {
          "attackingTeam": {
            "value": 18
          },
          "cause": "ELIMINATION",
          "defendingTeam": {
            "value": 19
          }
        },
        "winningTeam": {
          "value": 18
        }
      },
      {
        "roundNumber": 12,
        "spikeModeResult": {
          "attackingTeam": {
            "value": 18
          },
          "cause": "ELIMINATION",
          "defendingTeam": {
            "value": 19
          }
        },
        "winningTeam": {
          "value": 19
        }
      },
      {
        "roundNumber": 13,
        "spikeModeResult": {
          "attackingTeam": {
            "value": 19
          },
          "cause": "SPIKE_DEFUSE",
          "defendingTeam": {
            "value": 18
          }
        },
        "winningTeam": {
          "value": 18
        }
      },
      {
        "roundNumber": 14,
        "spikeModeResult": {
          "attackingTeam": {
            "value": 18
          },
          "cause": "SPIKE_DEFUSE",
          "defendingTeam": {
            "value": 19
          }
        },
        "winningTeam": {
          "value": 18
        }
      }
    ],
    "roundsToWin": 13,
    "defendingTeam": {
      "value": 18
    }
  }
}
```

```json
{
  "state": "DRAW",
  "spikeMode": {
    "currentRound": 1,
    "attackingTeam": {
      "value": 18
    },
    "completedRounds": [],
    "roundsToWin": 13,
    "defendingTeam": {
      "value": 19
    }
  }
}
```

```json
{
  "state": "UNSUPPORTED",
  "spikeMode": {
    "currentRound": 8,
    "attackingTeam": {
      "value": 16
    },
    "completedRounds": [
      {
        "roundNumber": 1,
        "spikeModeResult": {
          "attackingTeam": {
            "value": 16
          },
          "cause": "ELIMINATION",
          "defendingTeam": {
            "value": 17
          }
        },
        "winningTeam": {
          "value": 17
        }
      },
      {
        "roundNumber": 2,
        "spikeModeResult": {
          "attackingTeam": {
            "value": 16
          },
          "cause": "ELIMINATION",
          "defendingTeam": {
            "value": 17
          }
        },
        "winningTeam": {
          "value": 16
        }
      },
      {
        "roundNumber": 3,
        "spikeModeResult": {
          "attackingTeam": {
            "value": 16
          },
          "cause": "ELIMINATION",
          "defendingTeam": {
            "value": 17
          }
        },
        "winningTeam": {
          "value": 16
        }
      },
      {
        "roundNumber": 4,
        "spikeModeResult": {
          "attackingTeam": {
            "value": 16
          },
          "cause": "ELIMINATION",
          "defendingTeam": {
            "value": 17
          }
        },
        "winningTeam": {
          "value": 17
        }
      },
      {
        "roundNumber": 5,
        "spikeModeResult": {
          "attackingTeam": {
            "value": 16
          },
          "cause": "ELIMINATION",
          "defendingTeam": {
            "value": 17
          }
        },
        "winningTeam": {
          "value": 16
        }
      },
      {
        "roundNumber": 6,
        "spikeModeResult": {
          "attackingTeam": {
            "value": 16
          },
          "cause": "ELIMINATION",
          "defendingTeam": {
            "value": 17
          }
        },
        "winningTeam": {
          "value": 16
        }
      },
      {
        "roundNumber": 7,
        "spikeModeResult": {
          "attackingTeam": {
            "value": 16
          },
          "cause": "SPIKE_DEFUSE",
          "defendingTeam": {
            "value": 17
          }
        },
        "winningTeam": {
          "value": 17
        }
      }
    ],
    "roundsToWin": 13,
    "defendingTeam": {
      "value": 17
    }
  }
}
```

```json
{
  "state": "UNSUPPORTED",
  "spikeMode": {
    "currentRound": 8,
    "attackingTeam": {
      "value": 17
    },
    "completedRounds": [
      {
        "roundNumber": 1,
        "spikeModeResult": {
          "attackingTeam": {
            "value": 17
          },
          "cause": "ELIMINATION",
          "defendingTeam": {
            "value": 18
          }
        },
        "winningTeam": {
          "value": 17
        }
      },
      {
        "roundNumber": 2,
        "spikeModeResult": {
          "attackingTeam": {
            "value": 17
          },
          "cause": "ELIMINATION",
          "defendingTeam": {
            "value": 18
          }
        },
        "winningTeam": {
          "value": 17
        }
      },
      {
        "roundNumber": 3,
        "spikeModeResult": {
          "attackingTeam": {
            "value": 17
          },
          "cause": "ELIMINATION",
          "defendingTeam": {
            "value": 18
          }
        },
        "winningTeam": {
          "value": 17
        }
      },
      {
        "roundNumber": 4,
        "spikeModeResult": {
          "attackingTeam": {
            "value": 17
          },
          "cause": "ELIMINATION",
          "defendingTeam": {
            "value": 18
          }
        },
        "winningTeam": {
          "value": 17
        }
      },
      {
        "roundNumber": 5,
        "spikeModeResult": {
          "attackingTeam": {
            "value": 17
          },
          "cause": "ELIMINATION",
          "defendingTeam": {
            "value": 18
          }
        },
        "winningTeam": {
          "value": 18
        }
      },
      {
        "roundNumber": 6,
        "spikeModeResult": {
          "attackingTeam": {
            "value": 17
          },
          "cause": "ELIMINATION",
          "defendingTeam": {
            "value": 18
          }
        },
        "winningTeam": {
          "value": 17
        }
      },
      {
        "roundNumber": 7,
        "spikeModeResult": {
          "attackingTeam": {
            "value": 17
          },
          "cause": "ELIMINATION",
          "defendingTeam": {
            "value": 18
          }
        },
        "winningTeam": {
          "value": 18
        }
      }
    ],
    "roundsToWin": 13,
    "defendingTeam": {
      "value": 18
    }
  }
}
```

```json
{
  "state": "DRAW",
  "spikeMode": {
    "currentRound": 23,
    "attackingTeam": {
      "value": 20
    },
    "completedRounds": [
      {
        "roundNumber": 1,
        "spikeModeResult": {
          "attackingTeam": {
            "value": 19
          },
          "cause": "ELIMINATION",
          "defendingTeam": {
            "value": 20
          }
        },
        "winningTeam": {
          "value": 20
        }
      },
      {
        "roundNumber": 2,
        "spikeModeResult": {
          "attackingTeam": {
            "value": 19
          },
          "cause": "ELIMINATION",
          "defendingTeam": {
            "value": 20
          }
        },
        "winningTeam": {
          "value": 20
        }
      },
      {
        "roundNumber": 3,
        "spikeModeResult": {
          "attackingTeam": {
            "value": 19
          },
          "cause": "DETONATE",
          "defendingTeam": {
            "value": 20
          }
        },
        "winningTeam": {
          "value": 19
        }
      },
      {
        "roundNumber": 4,
        "spikeModeResult": {
          "attackingTeam": {
            "value": 19
          },
          "cause": "TIME_EXPIRED",
          "defendingTeam": {
            "value": 20
          }
        },
        "winningTeam": {
          "value": 20
        }
      },
      {
        "roundNumber": 5,
        "spikeModeResult": {
          "attackingTeam": {
            "value": 19
          },
          "cause": "ELIMINATION",
          "defendingTeam": {
            "value": 20
          }
        },
        "winningTeam": {
          "value": 19
        }
      },
      {
        "roundNumber": 6,
        "spikeModeResult": {
          "attackingTeam": {
            "value": 19
          },
          "cause": "ELIMINATION",
          "defendingTeam": {
            "value": 20
          }
        },
        "winningTeam": {
          "value": 19
        }
      },
      {
        "roundNumber": 7,
        "spikeModeResult": {
          "attackingTeam": {
            "value": 19
          },
          "cause": "ELIMINATION",
          "defendingTeam": {
            "value": 20
          }
        },
        "winningTeam": {
          "value": 19
        }
      },
      {
        "roundNumber": 8,
        "spikeModeResult": {
          "attackingTeam": {
            "value": 19
          },
          "cause": "ELIMINATION",
          "defendingTeam": {
            "value": 20
          }
        },
        "winningTeam": {
          "value": 19
        }
      },
      {
        "roundNumber": 9,
        "spikeModeResult": {
          "attackingTeam": {
            "value": 19
          },
          "cause": "ELIMINATION",
          "defendingTeam": {
            "value": 20
          }
        },
        "winningTeam": {
          "value": 20
        }
      },
      {
        "roundNumber": 10,
        "spikeModeResult": {
          "attackingTeam": {
            "value": 19
          },
          "cause": "ELIMINATION",
          "defendingTeam": {
            "value": 20
          }
        },
        "winningTeam": {
          "value": 19
        }
      },
      {
        "roundNumber": 11,
        "spikeModeResult": {
          "attackingTeam": {
            "value": 19
          },
          "cause": "ELIMINATION",
          "defendingTeam": {
            "value": 20
          }
        },
        "winningTeam": {
          "value": 19
        }
      },
      {
        "roundNumber": 12,
        "spikeModeResult": {
          "attackingTeam": {
            "value": 19
          },
          "cause": "ELIMINATION",
          "defendingTeam": {
            "value": 20
          }
        },
        "winningTeam": {
          "value": 20
        }
      },
      {
        "roundNumber": 13,
        "spikeModeResult": {
          "attackingTeam": {
            "value": 20
          },
          "cause": "ELIMINATION",
          "defendingTeam": {
            "value": 19
          }
        },
        "winningTeam": {
          "value": 20
        }
      },
      {
        "roundNumber": 14,
        "spikeModeResult": {
          "attackingTeam": {
            "value": 20
          },
          "cause": "ELIMINATION",
          "defendingTeam": {
            "value": 19
          }
        },
        "winningTeam": {
          "value": 19
        }
      },
      {
        "roundNumber": 15,
        "spikeModeResult": {
          "attackingTeam": {
            "value": 20
          },
          "cause": "ELIMINATION",
          "defendingTeam": {
            "value": 19
          }
        },
        "winningTeam": {
          "value": 19
        }
      },
      {
        "roundNumber": 16,
        "spikeModeResult": {
          "attackingTeam": {
            "value": 20
          },
          "cause": "ELIMINATION",
          "defendingTeam": {
            "value": 19
          }
        },
        "winningTeam": {
          "value": 19
        }
      },
      {
        "roundNumber": 17,
        "spikeModeResult": {
          "attackingTeam": {
            "value": 20
          },
          "cause": "ELIMINATION",
          "defendingTeam": {
            "value": 19
          }
        },
        "winningTeam": {
          "value": 19
        }
      },
      {
        "roundNumber": 18,
        "spikeModeResult": {
          "attackingTeam": {
            "value": 20
          },
          "cause": "ELIMINATION",
          "defendingTeam": {
            "value": 19
          }
        },
        "winningTeam": {
          "value": 20
        }
      },
      {
        "roundNumber": 19,
        "spikeModeResult": {
          "attackingTeam": {
            "value": 20
          },
          "cause": "DETONATE",
          "defendingTeam": {
            "value": 19
          }
        },
        "winningTeam": {
          "value": 20
        }
      },
      {
        "roundNumber": 20,
        "spikeModeResult": {
          "attackingTeam": {
            "value": 20
          },
          "cause": "DETONATE",
          "defendingTeam": {
            "value": 19
          }
        },
        "winningTeam": {
          "value": 20
        }
      },
      {
        "roundNumber": 21,
        "spikeModeResult": {
          "attackingTeam": {
            "value": 20
          },
          "cause": "DETONATE",
          "defendingTeam": {
            "value": 19
          }
        },
        "winningTeam": {
          "value": 20
        }
      },
      {
        "roundNumber": 22,
        "spikeModeResult": {
          "attackingTeam": {
            "value": 20
          },
          "cause": "SPIKE_DEFUSE",
          "defendingTeam": {
            "value": 19
          }
        },
        "winningTeam": {
          "value": 19
        }
      }
    ],
    "roundsToWin": 13,
    "defendingTeam": {
      "value": 19
    }
  }
}
```
