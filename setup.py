from setuptools import setup, find_packages

setup(
    name="my-microservice-app",
    version="1.0.0",
    packages=find_packages(),
    install_requires=[
        "Flask==2.2.2",
    ],
    entry_points={
        'console_scripts': [
            'run-app = app.main:app.run',
        ],
    },
)
