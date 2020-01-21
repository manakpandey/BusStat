from django.http import HttpResponse, JsonResponse
from django.views.decorators.csrf import csrf_exempt
from rest_framework.parsers import JSONParser
from smartbus.models import Bus, Stops, User, Token
from smartbus.serializers import BusSerializer, StopsSerializer, UserSerializer, TokenSerializer
import random


@csrf_exempt
def bus_list(request):
    """
    List all code snippets, or create a new snippet.
    """
    if request.method == 'GET':
        snippets = Bus.objects.all()
        serializer = BusSerializer(snippets, many=True)
        return JsonResponse(serializer.data, safe=False)

    elif request.method == 'POST':
        data = JSONParser().parse(request)
        serializer = BusSerializer(data=data)
        if serializer.is_valid():
            serializer.save()
            return JsonResponse(serializer.data, status=201)
        return JsonResponse(serializer.errors, status=400)


@csrf_exempt
def bus_detail(request, pk):
    """
    Retrieve, update or delete a code snippet.
    """
    try:
        snippet = Bus.objects.get(pk=pk)
    except Bus.DoesNotExist:
        return HttpResponse(status=404)

    if request.method == 'GET':
        serializer = BusSerializer(snippet)
        return JsonResponse(serializer.data)

    elif request.method == 'PUT':
        data = JSONParser().parse(request)
        serializer = BusSerializer(snippet, data=data)
        if serializer.is_valid():
            serializer.save()
            return JsonResponse(serializer.data)
        return JsonResponse(serializer.errors, status=400)

    elif request.method == 'DELETE':
        snippet.delete()
        return HttpResponse(status=204)


@csrf_exempt
def bus_search(request, src, dest):
    if request.method == 'GET':
        snippets = Bus.objects.filter(src=src, dest=dest)
        serializer = BusSerializer(snippets, many=True)
        return JsonResponse(serializer.data, safe=False)


@csrf_exempt
def stops_list(request):
    if request.method == 'GET':
        snippets = Stops.objects.all()
        serializer = StopsSerializer(snippets, many=True)
        return JsonResponse(serializer.data, safe=False)

    elif request.method == 'POST':
        data = JSONParser().parse(request)
        serializer = StopsSerializer(data=data)
        if serializer.is_valid():
            serializer.save()
            return JsonResponse(serializer.data, status=201)
        return JsonResponse(serializer.errors, status=400)


@csrf_exempt
def stops_detail(request, pk):
    """
    Retrieve, update or delete a code snippet.
    """
    try:
        snippet = Stops.objects.get(pk=pk)
    except Stops.DoesNotExist:
        return JsonResponse({"invalid": 1})

    if request.method == 'GET':
        serializer = StopsSerializer(snippet)
        return JsonResponse(serializer.data)

    elif request.method == 'PUT':
        data = JSONParser().parse(request)
        serializer = StopsSerializer(snippet, data=data)
        if serializer.is_valid():
            serializer.save()
            return JsonResponse(serializer.data)
        return JsonResponse(serializer.errors, status=400)

    elif request.method == 'DELETE':
        snippet.delete()
        return HttpResponse(status=204)


@csrf_exempt
def user_ops(request, pk):
    """
    Retrieve, update or delete a code snippet.
    """
    try:
        snippet = User.objects.get(pk=pk)
    except Stops.DoesNotExist:
        return HttpResponse(status=404)

    if request.method == 'PUT':
        data = JSONParser().parse(request)
        serializer = UserSerializer(snippet, data=data)
        if serializer.is_valid():
            serializer.save()
            return JsonResponse(serializer.data)
        return JsonResponse(serializer.errors, status=400)

    elif request.method == 'DELETE':
        snippet.delete()
        return HttpResponse(status=204)


@csrf_exempt
def add_user(request):
    if request.method == 'POST':
        data = JSONParser().parse(request)
        serializer = UserSerializer(data=data)
        if serializer.is_valid():
            serializer.save()
            return JsonResponse(serializer.data, status=201)
        return JsonResponse(serializer.errors, status=400)


@csrf_exempt
def verify_user(request):
    if request.method == 'POST':
        data = JSONParser().parse(request)
        try:
            user = User.objects.get(pk=data['id'])
        except User.DoesNotExist:
            return JsonResponse({'access': 'denied'}, status=200)
        serializer = UserSerializer(user)
        if serializer.data['password'] == data['password']:
            return JsonResponse(serializer.data, status=201)
        else:
            return JsonResponse({'access': 'denied'}, status=200)


@csrf_exempt
def generate_token(request):
    if request.method == 'POST':
        data = JSONParser().parse(request)
        while True:
            data['id'] = random.randint(111111, 999999)
            serializer = TokenSerializer(data=data)
            if serializer.is_valid():
                serializer.save()
                return JsonResponse(serializer.data, status=201)


@csrf_exempt
def use_token(request, pk):
    try:
        snippet = Token.objects.get(pk=pk)
    except Token.DoesNotExist:
        return JsonResponse({'status': 'invalid'})

    if request.method == 'GET':
        serializer = TokenSerializer(snippet)
        print(serializer.data)
        user = User.objects.get(pk=serializer.data['username'])
        user_ser = UserSerializer(user)
        amount = serializer.data["amount"]
        if serializer.data["amount"] <= user_ser.data["wallet_balance"]:
            user_update = {"id": user_ser.data["id"],
                           "password": user_ser.data["password"],
                           "wallet_balance": user_ser.data["wallet_balance"] - serializer.data["amount"]}

            user_serializer = UserSerializer(user, data=user_update)
            if user_serializer.is_valid():
                user_serializer.save()
                snippet.delete()
                return JsonResponse({"username": user_update["id"], "amount": amount, "status": "success"}, status=201)
            else:
                return JsonResponse({'status': 'invalid'}, status=200)






