"""empty message

Revision ID: 33971cfa531d
Revises: dbfa80d7b316
Create Date: 2020-12-03 04:06:50.148739

"""
from alembic import op
import sqlalchemy as sa


# revision identifiers, used by Alembic.
revision = '33971cfa531d'
down_revision = 'dbfa80d7b316'
branch_labels = None
depends_on = None


def upgrade():
    # ### commands auto generated by Alembic - please adjust! ###
    op.add_column('ingredient', sa.Column('image', sa.String(length=255, collation='utf8_unicode_ci'), nullable=True))
    # ### end Alembic commands ###


def downgrade():
    # ### commands auto generated by Alembic - please adjust! ###
    op.drop_column('ingredient', 'image')
    # ### end Alembic commands ###