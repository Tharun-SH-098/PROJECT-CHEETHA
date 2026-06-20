from transformers import AutoTokenizer, AutoModelForCausalLM

model_path = "./fine_tuned_model"

tokenizer = AutoTokenizer.from_pretrained(model_path)
model = AutoModelForCausalLM.from_pretrained(model_path)

prompt = "Artificial Intelligence"

inputs = tokenizer(prompt, return_tensors="pt")

outputs = model.generate(
    **inputs,
    max_length=100,
    do_sample=True,
    top_k=50,
    top_p=0.95,
    temperature=0.8
)

print(tokenizer.decode(outputs[0], skip_special_tokens=True))