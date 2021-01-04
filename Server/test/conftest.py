import sys, os
import pytest

parent_dir = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))
sys.path.insert(0, parent_dir)

from main import create_app


@pytest.fixture(scope="module")
def setup():
    app = create_app('test')
    app.config['test'] = True
    return app.test_client()
