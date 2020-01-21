from rest_framework import serializers
from smartbus.models import Bus, Stops, User, Token


class BusSerializer(serializers.ModelSerializer):
    class Meta:
        model = Bus
        fields = ['id', 'number', 'src', 'dest', 'price', 'capacity', 'occupied', 'curr_lat', 'curr_lon', 'avg_speed']


class StopsSerializer(serializers.ModelSerializer):
    class Meta:
        model = Stops
        fields = ['id', 'lat', 'lon']


class UserSerializer(serializers.ModelSerializer):
    class Meta:
        model = User
        fields = ['id', 'password', 'wallet_balance']


class TokenSerializer(serializers.ModelSerializer):
    class Meta:
        model = Token
        fields = ['id', 'username', 'amount']