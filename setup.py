#!/usr/bin/env python
import argparse
import os
from subprocess import run

BASE_PATH = os.path.dirname(os.path.abspath(__file__))
WRAPPER_SRC_PATH = os.path.join(BASE_PATH, 'src/main/rust/grean.rs')
WRAPPER_DEST_PATH = os.path.join(BASE_PATH, 'src/dist/bin')
WRAPPER_SETUP_TASK = ['rustc', WRAPPER_SRC_PATH, '--out-dir', WRAPPER_DEST_PATH, '-C', 'opt-level=3']


class Command:

    def __init__(self) -> None:
        # Construct the argument parser
        options = {
            'description': 'Prepares the workspace.',
        }
        parser = argparse.ArgumentParser(**options)

        # Add the arguments to the parser
        arguments = [
            [
                [
                    '-w',
                    '--wrapper',
                ],
                {
                    'action': 'store_true',
                    'help': 'Compiles the wrapper.',
                },
            ],
        ]
        for argument in arguments:
            parser.add_argument(*argument[0], **argument[1])

        # Parse the arguments from the system
        self.args = parser.parse_args()

    def handle(self) -> None:
        def wrapper() -> None:
            # Compile the wrapper
            run(WRAPPER_SETUP_TASK)

        if self.args.wrapper:
            wrapper()


if __name__ == '__main__':
    Command().handle()
