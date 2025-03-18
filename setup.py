#!/usr/bin/env python
import argparse
import os
from subprocess import run


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

    def run(self) -> None:
        base_path = os.path.dirname(os.path.abspath(__file__))

        def wrapper() -> None:
            arguments = [
                'rustc',
                os.path.join(base_path, 'src/main/rust/grean.rs'),
                '--out-dir',
                os.path.join(base_path, 'src/dist/bin'),
                '-C',
                'opt-level=3',
            ]

            # Compile the wrapper
            run(arguments)

        if self.args.wrapper:
            wrapper()


if __name__ == '__main__':
    Command().run()
