# Generated by Django 2.2.6 on 2019-11-08 11:53

from django.conf import settings
from django.db import migrations, models
import django.db.models.deletion
import django.utils.timezone


class Migration(migrations.Migration):

    initial = True

    dependencies = [
        migrations.swappable_dependency(settings.AUTH_USER_MODEL),
    ]

    operations = [
        migrations.CreateModel(
            name='Activity',
            fields=[
                ('act_id', models.AutoField(primary_key=True, serialize=False)),
                ('title', models.CharField(max_length=50)),
                ('content', models.TextField(blank=True, null=True)),
                ('longterm', models.CharField(blank=True, max_length=1, null=True)),
                ('outside', models.CharField(blank=True, max_length=1, null=True)),
                ('address', models.CharField(blank=True, max_length=100, null=True)),
                ('latitude', models.FloatField(blank=True, null=True)),
                ('longitude', models.FloatField(blank=True, null=True)),
            ],
            options={
                'db_table': 'activity',
            },
        ),
        migrations.CreateModel(
            name='LargeCat',
            fields=[
                ('lcat_name', models.CharField(db_column='Lcat_name', max_length=50, primary_key=True, serialize=False)),
            ],
            options={
                'db_table': 'large_cat',
            },
        ),
        migrations.CreateModel(
            name='Profile',
            fields=[
                ('id', models.AutoField(auto_created=True, primary_key=True, serialize=False, verbose_name='ID')),
                ('nickname', models.CharField(blank=True, max_length=30, null=True)),
                ('score', models.IntegerField(blank=True, null=True)),
                ('activity', models.IntegerField(blank=True, null=True)),
                ('sociality', models.IntegerField(blank=True, null=True)),
                ('gender', models.CharField(blank=True, max_length=10, null=True)),
                ('age', models.IntegerField(blank=True, null=True)),
                ('created', models.DateTimeField(blank=True, default=django.utils.timezone.now, null=True)),
                ('user', models.OneToOneField(on_delete=django.db.models.deletion.CASCADE, to=settings.AUTH_USER_MODEL)),
            ],
            options={
                'db_table': 'profile',
            },
        ),
        migrations.CreateModel(
            name='Feed',
            fields=[
                ('id', models.AutoField(auto_created=True, primary_key=True, serialize=False, verbose_name='ID')),
                ('title', models.CharField(blank=True, max_length=50, null=True)),
                ('content', models.TextField(blank=True, null=True)),
                ('time', models.DateTimeField(blank=True, null=True)),
                ('act', models.ForeignKey(on_delete=django.db.models.deletion.DO_NOTHING, to='api.Activity')),
                ('author', models.ForeignKey(blank=True, null=True, on_delete=django.db.models.deletion.DO_NOTHING, to='api.Profile')),
            ],
            options={
                'db_table': 'feed',
            },
        ),
        migrations.CreateModel(
            name='Category',
            fields=[
                ('cat_name', models.CharField(max_length=50, primary_key=True, serialize=False)),
                ('activity_rate', models.IntegerField(blank=True, null=True)),
                ('sociality_rate', models.IntegerField(blank=True, null=True)),
                ('lcat_name', models.ForeignKey(blank=True, db_column='Lcat_name', null=True, on_delete=django.db.models.deletion.DO_NOTHING, to='api.LargeCat')),
            ],
            options={
                'db_table': 'category',
            },
        ),
        migrations.CreateModel(
            name='Assemble',
            fields=[
                ('id', models.AutoField(auto_created=True, primary_key=True, serialize=False, verbose_name='ID')),
                ('title', models.CharField(blank=True, max_length=50, null=True)),
                ('content', models.TextField(blank=True, null=True)),
                ('time', models.DateTimeField(blank=True, null=True)),
                ('photo', models.TextField(blank=True, null=True)),
                ('author', models.ForeignKey(blank=True, null=True, on_delete=django.db.models.deletion.DO_NOTHING, to='api.Profile')),
            ],
            options={
                'db_table': 'assemble',
            },
        ),
        migrations.AddField(
            model_name='activity',
            name='category',
            field=models.ForeignKey(db_column='category', on_delete=django.db.models.deletion.DO_NOTHING, to='api.Category'),
        ),
        migrations.CreateModel(
            name='Takes',
            fields=[
                ('id', models.ForeignKey(db_column='id', on_delete=django.db.models.deletion.DO_NOTHING, primary_key=True, serialize=False, to='api.Profile')),
                ('date', models.DateField()),
                ('act', models.ForeignKey(on_delete=django.db.models.deletion.DO_NOTHING, to='api.Activity')),
            ],
            options={
                'db_table': 'takes',
                'unique_together': {('id', 'act', 'date')},
            },
        ),
        migrations.CreateModel(
            name='PreCat',
            fields=[
                ('id', models.AutoField(auto_created=True, primary_key=True, serialize=False, verbose_name='ID')),
                ('user', models.ForeignKey(on_delete=django.db.models.deletion.DO_NOTHING, serialize=False, to='api.Profile')),
                ('cat_name', models.ForeignKey(db_column='cat_name', on_delete=django.db.models.deletion.DO_NOTHING, to='api.Category')),
            ],
            options={
                'db_table': 'pre_cat',
                'unique_together': {('user', 'cat_name')},
            },
        ),
    ]
